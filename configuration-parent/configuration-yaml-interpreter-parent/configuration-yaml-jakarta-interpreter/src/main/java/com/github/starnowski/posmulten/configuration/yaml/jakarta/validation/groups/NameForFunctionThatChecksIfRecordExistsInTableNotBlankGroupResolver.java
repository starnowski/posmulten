package com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.groups;

import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractPrimaryKeyDefinition;
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.SharedSchemaContextConfiguration;

public class NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver implements ValidatorGroupResolver<AbstractPrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank> {


    public Class<AbstractPrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank> resolveForSharedSchemaContextConfiguration(SharedSchemaContextConfiguration sharedSchemaContextConfiguration, ValidatorGroupResolverContext context) {
        return Boolean.TRUE.equals(sharedSchemaContextConfiguration.getCreateForeignKeyConstraintWithTenantColumn()) ? null : AbstractPrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank.class;
    }
}
