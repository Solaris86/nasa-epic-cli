package com.plenigo.nasaepiccli.validation;

import com.plenigo.nasaepiccli.model.ImageType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ImageTypeValidator implements ConstraintValidator<ValidImageType, String> {

    private static final Logger logger = LoggerFactory.getLogger(ImageTypeValidator.class);

    @Override
    public void initialize(ValidImageType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(type)) {
            return true;
        }

        boolean imageTypeMatches = Arrays.stream(ImageType.values()).anyMatch(imageType -> imageType.getName().equals(type));
        if (!imageTypeMatches) {
            logger.error("Unsupported image type passed, [{}]", type);
        }

        return imageTypeMatches;
    }
}
