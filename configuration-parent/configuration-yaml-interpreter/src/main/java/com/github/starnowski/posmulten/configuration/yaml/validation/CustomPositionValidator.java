package com.github.starnowski.posmulten.configuration.yaml.validation;

import com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomPositionValidator implements ConstraintValidator<CustomPositionValidValue, CustomDefinitionEntry> {
    @Override
    public boolean isValid(CustomDefinitionEntry customDefinitionEntry, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
