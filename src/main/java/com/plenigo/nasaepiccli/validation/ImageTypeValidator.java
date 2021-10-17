package com.plenigo.nasaepiccli.validation;

import com.plenigo.nasaepiccli.model.ImageType;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ImageTypeValidator implements ConstraintValidator<ValidImageType, String> {

    @Override
    public void initialize(ValidImageType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(type) || Arrays.stream(ImageType.values()).anyMatch(imageType -> imageType.getName().equals(type));
    }
}
