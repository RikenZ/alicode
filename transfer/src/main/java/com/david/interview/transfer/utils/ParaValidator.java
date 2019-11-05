package com.david.interview.transfer.utils;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Slf4j
public class ParaValidator {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static void validate(Object object) {
        if (null == object) {
            IllegalArgumentException exception = new IllegalArgumentException("parameter null error");
            throw exception;
        }
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if (constraintViolations.size() > 0) {
            IllegalArgumentException exception = new IllegalArgumentException(constraintViolations.iterator().next().getMessage());
            throw exception;
        }
    }
}
