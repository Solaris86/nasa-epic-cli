package com.plenigo.nasaepiccli.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CapturedDateValidator implements ConstraintValidator<ValidCapturedDate, String> {

    @Override
    public void initialize(ValidCapturedDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String capturedDate, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(capturedDate)) {
            return true;
        }

        try {
            LocalDate.parse(capturedDate);
        } catch (DateTimeParseException ex) {
            return false;
        }

        return true;
    }
}
