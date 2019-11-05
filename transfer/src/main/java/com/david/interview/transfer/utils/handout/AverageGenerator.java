package com.david.interview.transfer.utils.handout;

import com.david.interview.transfer.enums.GenType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AverageGenerator implements HandoutAmountGenerator {
    @Override
    public List<BigDecimal> gen(BigDecimal totalAmount, Integer size, Integer scale, BigDecimal minAmount) {
        //TODO  空实现
        return new ArrayList<>(size);
    }

    @Override
    public GenType getType() {
        return GenType.AVERAGE;
    }
}
