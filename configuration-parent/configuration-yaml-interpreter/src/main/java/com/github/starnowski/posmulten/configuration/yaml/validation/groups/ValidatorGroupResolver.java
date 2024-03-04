package com.github.starnowski.posmulten.configuration.yaml.validation.groups;

import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

public interface ValidatorGroupResolver<T> {

    Class<T> resolveForSharedSchemaContextConfiguration(SharedSchemaContextConfiguration sharedSchemaContextConfiguration);
}
