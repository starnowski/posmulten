package com.github.starnowski.posmulten.configuration.yaml.validation;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, String> {

    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(EnumNamePattern constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        if (Stream.of(this.enumClass.getEnumConstants()).map(e -> ((Enum) e).name()).noneMatch(name -> name.equals(value))) {
            HibernateConstraintValidatorContext hibernateContext = constraintValidatorContext.unwrap(
                    HibernateConstraintValidatorContext.class );
            hibernateContext.addExpressionVariable("enumValues", Stream.of(this.enumClass.getEnumConstants()).map(e -> ((Enum) e).name()).collect(Collectors.joining(", ")));
            return false;
        }
        return true;
    }
}
