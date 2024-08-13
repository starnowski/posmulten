package com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.groups;

import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.SharedSchemaContextConfiguration;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ValidatorGroupsResolver {

    private final List<ValidatorGroupResolver> validatorGroupResolvers;

    public ValidatorGroupsResolver() {
        this(Collections.singletonList(new NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver()));
    }

    public ValidatorGroupsResolver(List<ValidatorGroupResolver> validatorGroupResolvers) {
        this.validatorGroupResolvers = validatorGroupResolvers;
    }

    List<ValidatorGroupResolver> getValidatorGroupResolvers() {
        return validatorGroupResolvers;
    }

    public List<Class> resolveForSharedSchemaContextConfiguration(SharedSchemaContextConfiguration sharedSchemaContextConfiguration, ValidatorGroupResolver.ValidatorGroupResolverContext validatorGroupResolverContext) {
        return validatorGroupResolvers.stream().map(resolver -> resolver.resolveForSharedSchemaContextConfiguration(sharedSchemaContextConfiguration, validatorGroupResolverContext)).collect(toList());
    }
}
