package com.david.interview.transfer.utils.handout;

import com.david.interview.transfer.enums.GenType;

import java.math.BigDecimal;
import java.util.List;

public interface HandoutAmountGenerator {
    List<BigDecimal> gen(BigDecimal totalAmount, Integer size, Integer scale, BigDecimal minAmount);

    GenType getType();
}
