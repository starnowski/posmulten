package com.github.starnowski.posmulten.configuration.yaml.validation.groups;

import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

import java.util.Arrays;
import java.util.List;

public class ValidatorGroupsResolver {

    private final List<ValidatorGroupResolver> validatorGroupResolvers;

    public ValidatorGroupsResolver() {
        this(Arrays.asList(new NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver()));
    }

    public ValidatorGroupsResolver(List<ValidatorGroupResolver> validatorGroupResolvers) {
        this.validatorGroupResolvers = validatorGroupResolvers;
    }

    List<ValidatorGroupResolver> getValidatorGroupResolvers() {
        return validatorGroupResolvers;
    }

    List<Class> resolveForSharedSchemaContextConfiguration(SharedSchemaContextConfiguration sharedSchemaContextConfiguration) {
        //ValidatorGroupResolver
        return null;
    }
}
