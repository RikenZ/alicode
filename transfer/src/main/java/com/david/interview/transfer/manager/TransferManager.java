package com.david.interview.transfer.manager;

import com.david.interview.transfer.dao.AccountDao;
import com.david.interview.transfer.dao.TransferDao;
import com.david.interview.transfer.enums.TransferStatus;
import com.david.interview.transfer.model.Account;
import com.david.interview.transfer.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class TransferManager {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;


    /**
     * 使用spring注解事务，设置隔离级别 如果报错直接回滚两个事务
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Transfer transferTransactional(Transfer transfer) {
        /**
         * 可能出现死锁的原因是,并发情况下对两个账户的操作无法保证其执行顺序
         *
         * 线程一执行的是：【账户A】给【账户B】转账
         * 线程二执行的是：【账户B】给【账户A】转账
         *
         * 如果两个转账动作同时执行,则会出现
         * 线程一会请求对【账户B】进行加锁,线程二会请求对【账户A】进行加锁
         * 由于此时的【账户A】已由线程一进行锁定,【账户B】已由线程二进行锁定
         * 此时就会产生死锁问题。
         *
         * 通过上面的问题描述可以发现，在一个转账的过程中【账户A】、【账户B】的执行是有序的
         * 但是在多个转账过程中【账户A】、【账户B】的执行是无序的。
         * 因此只要保证每次执行转账时对账户的操作按照顺序执行就可以避免死锁
         *
         * 例如在系统中对所有的账户进行排序：
         * 【账户A】、【账户B】、【账户C】、【账户D】、【账户E】、【账户F】
         *
         * 线程一执行【账户A】给【账户B】转账逻辑：
         * 1. 开启事务
         * 2. 【账户A】的余额减去
         * 3. 【账户B】的余额增加
         * 4. 提交事务
         *
         * 线程二执行【账户B】给【账户A】转账逻辑：
         * 1. 开启事务
         * 2. 【账户A】的余额增加
         * 3. 【账户B】的余额减去
         * 4. 提交事务
         *
         * 将账户进行排序，保证转账过程中【账户A】始终先于【账户B】，这样就可以避免产生死锁问题。
         *
         *
         * 解决方案：账户表中添加自增id字段,用于账户排序 id小的先执行
         *
         * */
        // 获取账户信息
        Account payerAccount = accountDao.load(transfer.getPayerAccount());
        if (payerAccount == null) {
            transfer.setFailureMsg("转出账户不存在");
            transfer.setStatus(TransferStatus.FAILED);
            transferDao.update(transfer);
            throw new RuntimeException("转出账户不存在");
        }
        if (transfer.getAmount().compareTo(payerAccount.getAvailableAmount()) > 0) {
            transfer.setFailureMsg("转出账户余额不足");
            transfer.setStatus(TransferStatus.FAILED);
            transferDao.update(transfer);
            throw new RuntimeException("转出账户余额不足");
        }
        Account payeeAccount = accountDao.load(transfer.getPayeeAccount());
        if (payeeAccount == null) {
            transfer.setFailureMsg("转入账户不存在");
            transfer.setStatus(TransferStatus.FAILED);
            transferDao.update(transfer);
            throw new RuntimeException("转入账户不存在");
        }
        // 判断账户执行顺序
        if (payerAccount.getId() > payeeAccount.getId()) {
            // 使用ForUpdate进行行锁
            payeeAccount = accountDao.loadForUpdate(payeeAccount.getAccountNo());
            // 转入账户收款操作
            BigDecimal payeeAvailableAmount = payeeAccount.getAvailableAmount().add(transfer.getAmount());
            payeeAccount.setAvailableAmount(payeeAvailableAmount);
            accountDao.update(payerAccount);
            // 使用ForUpdate进行行锁
            payerAccount = accountDao.loadForUpdate(payerAccount.getAccountNo());
            // 转出账户扣款操作
            BigDecimal payerAvailableAmount = payerAccount.getAvailableAmount().subtract(transfer.getAmount());
            payerAccount.setAvailableAmount(payerAvailableAmount);
            accountDao.update(payerAccount);
        } else {
            // 使用ForUpdate进行行锁
            payerAccount = accountDao.loadForUpdate(payerAccount.getAccountNo());
            // 转出账户扣款操作
            BigDecimal payerAvailableAmount = payerAccount.getAvailableAmount().subtract(transfer.getAmount());
            payerAccount.setAvailableAmount(payerAvailableAmount);
            accountDao.update(payerAccount);
            // 使用ForUpdate进行行锁
            payeeAccount = accountDao.loadForUpdate(payeeAccount.getAccountNo());
            // 转入账户收款操作
            BigDecimal payeeAvailableAmount = payeeAccount.getAvailableAmount().add(transfer.getAmount());
            payeeAccount.setAvailableAmount(payeeAvailableAmount);
            accountDao.update(payerAccount);

        }
        // 转账成功,更新状态
        transfer.setStatus(TransferStatus.SUCCESS);
        transfer.setTimeSuccess(System.currentTimeMillis());
        transferDao.update(transfer);
        return transfer;
    }
}
