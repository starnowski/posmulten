package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

import static java.util.stream.Collectors.toList;

public class SharedSchemaContextConfigurationMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration, SharedSchemaContextConfiguration> {

    private final TableEntryMapper tableEntryMapper = new TableEntryMapper();
    private final ValidTenantValueConstraintConfigurationMapper validTenantValueConstraintConfigurationMapper = new ValidTenantValueConstraintConfigurationMapper();

    @Override
    public SharedSchemaContextConfiguration map(com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration input) {
        return input == null ? null : new SharedSchemaContextConfiguration()
                .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(input.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables())
                .setCurrentTenantIdProperty(input.getCurrentTenantIdProperty())
                .setCurrentTenantIdPropertyType(input.getCurrentTenantIdPropertyType())
                .setDefaultSchema(input.getDefaultSchema())
                .setDefaultTenantIdColumn(input.getDefaultTenantIdColumn())
                .setEqualsCurrentTenantIdentifierFunctionName(input.getEqualsCurrentTenantIdentifierFunctionName())
                .setForceRowLevelSecurityForTableOwner(input.getForceRowLevelSecurityForTableOwner())
                .setGetCurrentTenantIdFunctionName(input.getGetCurrentTenantIdFunctionName())
                .setGrantee(input.getGrantee())
                .setSetCurrentTenantIdFunctionName(input.getSetCurrentTenantIdFunctionName())
                .setTenantHasAuthoritiesFunctionName(input.getTenantHasAuthoritiesFunctionName())
                .setTables(input.getTables() == null ? null : input.getTables().stream().map(tableEntry -> tableEntryMapper.map(tableEntry)).collect(toList()))
                .setValidTenantValueConstraint(validTenantValueConstraintConfigurationMapper.map(input.getValidTenantValueConstraint()))
                ;
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration unmap(SharedSchemaContextConfiguration output) {
        return null;
    }
}
