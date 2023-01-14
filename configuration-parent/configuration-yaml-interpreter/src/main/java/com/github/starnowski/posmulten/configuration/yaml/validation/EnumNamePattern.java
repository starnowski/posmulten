package com.github.starnowski.posmulten.configuration.yaml.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Implementation used code from article https://www.baeldung.com/javax-validations-enums
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumNamePatternValidator.class)
public @interface EnumNamePattern {

    Class<? extends java.lang.Enum> enumType();

    String message() default "{error.enum.values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}