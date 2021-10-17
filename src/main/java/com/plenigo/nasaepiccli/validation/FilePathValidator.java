package com.plenigo.nasaepiccli.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilePathValidator implements ConstraintValidator<ValidFilePath, String> {

    private static final Logger logger = LoggerFactory.getLogger(FilePathValidator.class);

    @Override
    public void initialize(ValidFilePath constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        boolean fileExists = Files.exists(Paths.get(path));
        if (!fileExists) {
            logger.error("Invalid path passed [{}]", path);
        }

        return fileExists;
    }

}
