package com.plenigo.nasaepiccli.validation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CapturedDateValidator implements ConstraintValidator<ValidCapturedDate, String> {

    private Logger logger = LoggerFactory.getLogger(CapturedDateValidator.class);

    @Override
    public void initialize(ValidCapturedDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String capturedDate, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(capturedDate)) {
            return true;
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(capturedDate);
        } catch (DateTimeParseException ex) {
            logger.error("Invalid date format passed [{}]", capturedDate);
            return false;
        }

        if (parsedDate.isAfter(LocalDate.now())) {
            logger.error("Passed date cannot be in the future [{}]", capturedDate);
            return false;
        }

        return true;
    }
}
