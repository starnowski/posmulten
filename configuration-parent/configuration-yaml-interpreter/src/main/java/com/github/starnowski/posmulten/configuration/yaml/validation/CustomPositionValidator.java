package com.github.starnowski.posmulten.configuration.yaml.validation;

import com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomPositionValidator implements ConstraintValidator<CustomPositionValidValue, CustomDefinitionEntry> {
    @Override
    public boolean isValid(CustomDefinitionEntry customDefinitionEntry, ConstraintValidatorContext constraintValidatorContext) {
        if (customDefinitionEntry.getPosition() == null) {
            return true;
        }
        boolean result = true;
        switch (customDefinitionEntry.getPosition()) {
            case AT_END:
            case AT_BEGINNING:
                result = true;
                break;
            case CUSTOM:
                result = customDefinitionEntry.getCustomPosition() != null && !customDefinitionEntry.getCustomPosition().trim().isEmpty();
        }
        return result;
    }
}
