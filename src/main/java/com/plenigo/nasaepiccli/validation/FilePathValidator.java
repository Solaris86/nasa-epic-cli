package com.plenigo.nasaepiccli.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilePathValidator implements ConstraintValidator<ValidFilePath, String> {

    @Override
    public void initialize(ValidFilePath constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        return Files.exists(Paths.get(path));
    }

}
