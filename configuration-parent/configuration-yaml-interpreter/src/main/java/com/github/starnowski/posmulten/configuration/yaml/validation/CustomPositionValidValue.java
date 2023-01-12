package com.github.starnowski.posmulten.configuration.yaml.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomPositionValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPositionValidValue {
    String message() default "{error.custom.definition.custom.position.value}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
