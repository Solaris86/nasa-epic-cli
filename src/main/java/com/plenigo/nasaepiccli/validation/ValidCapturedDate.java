package com.plenigo.nasaepiccli.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CapturedDateValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCapturedDate {

    String message() default "Invalid captured date format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
