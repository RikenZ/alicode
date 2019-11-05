package com.david.interview.transfer.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ReceiveHandoutDTO implements Serializable {
    @NotEmpty(message = "红包id不为空")
    private String id;
    @NotEmpty(message = "账号不能为空")
    private String account;
}
