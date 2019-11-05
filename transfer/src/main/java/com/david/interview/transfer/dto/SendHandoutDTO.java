package com.david.interview.transfer.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SendHandoutDTO implements Serializable {
    @NotEmpty(message = "红包id不为空")
    private String id;
    //红包个数
    @NotNull(message = "红包个数不能为空")
    @Min(value = 1, message = "红包个数不能小于1")
    private Integer count;
    /**
     * 交易金额
     */
    @NotNull(message = "红包金额不能为空")
    @Digits(integer = 10, fraction = 2, message = "整数部分不超过10位,小数点后不超过2位")
    private BigDecimal amount;
    /**
     * 转出账号
     */
    @NotEmpty(message = "付款账号不能为空")
    private String payerAccount;
}
