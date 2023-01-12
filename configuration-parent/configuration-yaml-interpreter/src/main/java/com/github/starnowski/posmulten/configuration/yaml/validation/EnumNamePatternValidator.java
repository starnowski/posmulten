package com.github.starnowski.posmulten.configuration.yaml.validation;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, Enum<?>> {

    private Class<? extends Enum> enumClass;
    private boolean required;

    @Override
    public void initialize(EnumNamePattern constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumType();
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Enum<?> anEnum, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = true;
        if (anEnum == null && this.required) {
            result = false;
        }
        if (result && !(anEnum.getClass() == this.enumClass)) {
            result = false;
        }
        if (!result) {
            HibernateConstraintValidatorContext hibernateContext = constraintValidatorContext.unwrap(
                    HibernateConstraintValidatorContext.class );
            hibernateContext.addExpressionVariable("enumValues", Stream.of(this.enumClass.getEnumConstants()).map(e -> ((Enum) e).name()).collect(Collectors.joining(", ")));
        }
        return result;
    }
}
