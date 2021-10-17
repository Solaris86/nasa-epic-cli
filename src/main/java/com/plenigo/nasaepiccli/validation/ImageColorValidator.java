package com.plenigo.nasaepiccli.validation;

import com.plenigo.nasaepiccli.model.ImageColor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ImageColorValidator implements ConstraintValidator<ValidImageColor, String> {

    private static final Logger logger = LoggerFactory.getLogger(ImageColorValidator.class);

    @Override
    public void initialize(ValidImageColor constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String imageColor, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(imageColor)) {
            return true;
        }

        boolean imageColorMatches = Arrays.stream(ImageColor.values()).anyMatch(color -> color.getColor().equals(imageColor));
        if (!imageColorMatches) {
            logger.error("Unsupported image color passed, [{}]", imageColor);
        }

        return imageColorMatches;
    }
}
