package com.david.interview.transfer.service;

import com.david.interview.transfer.dto.TransferRequestParamDTO;
import com.david.interview.transfer.model.Transfer;

public interface TransferService {

    /**
     * 转账接口
     *
     * @param paramDTO 转账请求参数
     * @return Transfer
     */
    Transfer transfer(TransferRequestParamDTO paramDTO);
}
