package com.plenigo.nasaepiccli.validation;

import com.plenigo.nasaepiccli.model.ImageColor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ImageColorValidator implements ConstraintValidator<ValidImageColor, String> {
    @Override
    public void initialize(ValidImageColor constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String imageColor, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(imageColor) || Arrays.stream(ImageColor.values()).anyMatch(color -> color.getColor().equals(imageColor));
    }
}
