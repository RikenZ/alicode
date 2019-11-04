package com.david.interview.transfer.model;

import com.david.interview.transfer.enums.TransferStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class Transfer implements Serializable {
    private static final long serialVersionUID = -6548319329571430698L;

    /**
     * 交易单号
     * */
    private String orderNo;

    /**
     * 交易金额
     * */
    private BigDecimal amount;

    /**
     * 转入账号
     * */
    private String payeeAccount;

    /**
     * 转出账号
     * */
    private String payerAccount;

    /**
     * 转账状态
     * */
    private TransferStatus status;

    /**
     * 转账记录创建时间
     * */
    private Long timeCreated;

    /**
     * 转账成功时间
     * */
    private Long timeSuccess;

    /**
     * 转账失败信息
     * */
    private String failureMsg;

}
