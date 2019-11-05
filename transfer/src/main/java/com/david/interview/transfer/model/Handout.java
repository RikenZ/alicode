package com.david.interview.transfer.model;

import com.david.interview.transfer.enums.HandOutStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class Handout implements Serializable {
    //红包id
    private String id;
    //红包个数
    private Integer count;
    //红包金额
    private BigDecimal amount;
    //付款账号
    private String payerAccount;
    //超时时间 秒
    private Long expire = 86400L;
    //状态
    private HandOutStatus status;
    //创建时间
    private Long timeCreated;
    //分配出的红包金额
    List<BigDecimal> handouts;
}
