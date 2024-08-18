package com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.groups;

import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.SharedSchemaContextConfiguration;

public interface ValidatorGroupResolver<T> {

    Class<T> resolveForSharedSchemaContextConfiguration(SharedSchemaContextConfiguration sharedSchemaContextConfiguration, ValidatorGroupResolverContext context);

    interface ValidatorGroupResolverContext {

    }
}
