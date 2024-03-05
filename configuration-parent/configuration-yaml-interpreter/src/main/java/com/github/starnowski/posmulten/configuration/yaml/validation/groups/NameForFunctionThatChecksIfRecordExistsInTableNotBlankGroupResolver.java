package com.github.starnowski.posmulten.configuration.yaml.validation.groups;

import com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

public class NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver implements ValidatorGroupResolver<PrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank> {


    @Override
    public Class<PrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank> resolveForSharedSchemaContextConfiguration(SharedSchemaContextConfiguration sharedSchemaContextConfiguration, ValidatorGroupResolverContext context) {
        return Boolean.TRUE.equals(sharedSchemaContextConfiguration.getCreateForeignKeyConstraintWithTenantColumn()) ? null : PrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank.class;
    }
}
