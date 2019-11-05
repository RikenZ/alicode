package com.david.interview.transfer.utils.handout;

import com.david.interview.transfer.enums.GenType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//使用单例加策略模式
public class HandoutAmountGeneratorContext {
    public static final Map<GenType, HandoutAmountGenerator> MAP = new ConcurrentHashMap<>();

    static {
        RandomGenerator randomGenerator = new RandomGenerator();
        AverageGenerator averageGenerator = new AverageGenerator();
        MAP.put(randomGenerator.getType(), randomGenerator);
        MAP.put(averageGenerator.getType(), averageGenerator);
    }

    private HandoutAmountGeneratorContext() {
    }

    public static HandoutAmountGenerator lookup(GenType type) {
        return MAP.get(type);
    }
}
