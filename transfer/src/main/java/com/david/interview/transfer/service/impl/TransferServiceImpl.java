package com.david.interview.transfer.service.impl;

import com.david.interview.transfer.dao.AccountDao;
import com.david.interview.transfer.dao.TransferDao;
import com.david.interview.transfer.dto.TransferRequestParamDTO;
import com.david.interview.transfer.enums.TransferStatus;
import com.david.interview.transfer.manager.TransferManager;
import com.david.interview.transfer.model.Transfer;
import com.david.interview.transfer.service.TransferService;
import com.david.interview.transfer.utils.ParaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    TransferDao transferDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    TransferManager transferManager;

    @Override
    public Transfer transfer(TransferRequestParamDTO paramDTO) {

        // 参数检查
        ParaValidator.validate(paramDTO);

        // 保证接口的幂等性,同一个交易号只能发起一次转账
        Transfer transfer = transferDao.load(paramDTO.getOrderNo());
        if (transfer != null) {
            return transfer;
        }
        // 创建转账对象
        transfer = generateTransfer(paramDTO);
        transferDao.create(transfer);

        // 转账事务
        transfer = transferManager.transferTransactional(transfer);

        return transfer;
    }

    private Transfer generateTransfer(TransferRequestParamDTO paramDTO) {
        Transfer transfer = new Transfer();
        transfer.setAmount(paramDTO.getAmount());
        transfer.setOrderNo(paramDTO.getOrderNo());
        transfer.setPayeeAccount(paramDTO.getPayeeAccount());
        transfer.setPayerAccount(paramDTO.getPayerAccount());
        transfer.setTimeCreated(System.currentTimeMillis());
        transfer.setStatus(TransferStatus.PROCESSING);
        return transfer;
    }
}
