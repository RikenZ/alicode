package com.david.interview.transfer.service.impl;

import com.david.interview.transfer.dao.AccountDao;
import com.david.interview.transfer.dao.HandoutDao;
import com.david.interview.transfer.dto.ReceiveHandoutDTO;
import com.david.interview.transfer.dto.SendHandoutDTO;
import com.david.interview.transfer.enums.GenType;
import com.david.interview.transfer.model.Account;
import com.david.interview.transfer.model.Handout;
import com.david.interview.transfer.service.HandoutService;
import com.david.interview.transfer.utils.Constant;
import com.david.interview.transfer.utils.handout.HandoutAmountGenerator;
import com.david.interview.transfer.utils.handout.HandoutAmountGeneratorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class HandoutServiceImpl implements HandoutService {
    @Autowired
    private HandoutDao handoutDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Executor executor;

    @Transactional
    @Override
    public Handout sendHandout(SendHandoutDTO dto) {
        String key = String.format(Constant.HANDOUT_FMT, dto.getId());
        if (redisTemplate.hasKey(key)) {
            return handoutDao.load(dto.getId());
        }
        //生成红包
        HandoutAmountGenerator gen = HandoutAmountGeneratorContext.lookup(GenType.RANDOM);
        List<BigDecimal> handouts = gen.gen(dto.getAmount(), dto.getCount(), 2, BigDecimal.valueOf(0.01D));
        //扣除红包金额
        Account account = accountDao.loadForUpdate(dto.getPayerAccount());
        account.setAvailableAmount(account.getAvailableAmount().subtract(dto.getAmount()));
        accountDao.update(account);
        //存红包到db
        Handout handout = new Handout()
                .setId(dto.getId())
                .setPayerAccount(dto.getPayerAccount())
                .setAmount(dto.getAmount())
                .setHandouts(handouts)
                .setCount(dto.getCount());
        handoutDao.create(handout);
        //缓存到redis
        redisTemplate.opsForList().leftPushAll(key, handouts);
        //调用发送通知接口
        return handout;
    }

    @Transactional
    @Override
    public BigDecimal receiveHandout(ReceiveHandoutDTO dto) {
        String id = dto.getId();
        String key = String.format(Constant.HANDOUT_FMT, id);
        //判断是否还有红包
        Long size = redisTemplate.opsForList().size(key);
        if (size <= 0) throw new RuntimeException("红包被抢完");
        //获取红包
        BigDecimal a = (BigDecimal) redisTemplate.opsForList().leftPop(key);
        //更新账户
        Account account = accountDao.loadForUpdate(dto.getAccount());
        account.setAvailableAmount(account.getAvailableAmount().add(a));
        accountDao.update(account);
        //异步更新 表数据
        executor.execute(() -> {
            Handout handout = handoutDao.load(id);
            List<BigDecimal> l = redisTemplate.opsForList().range(key, 0, handout.getCount());
            handoutDao.update(new Handout().setId(id).setHandouts(l));
        });
        return a;
    }

    //起定时任务
    @Transactional
    @Override
    public void refund(String id) {
        String key = String.format(Constant.HANDOUT_FMT, id);
        Handout handout = handoutDao.load(id);
        Integer dbCount = handout.getCount();
        List<BigDecimal> range = redisTemplate.opsForList().range(key, 0, dbCount);
        List<BigDecimal> rest = range.stream().filter(Objects::nonNull).collect(Collectors.toList());
        //有没领完的红包 退回原账号
        if (rest.size() > 0) {
            BigDecimal restAmount = rest.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            Account account = accountDao.loadForUpdate(handout.getPayerAccount());
            account.setAvailableAmount(account.getAvailableAmount().add(restAmount));
            accountDao.update(account);
        }
    }
}
