package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

import static com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue.valueOf;
import static java.util.stream.Collectors.toList;

public class SharedSchemaContextConfigurationMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration, SharedSchemaContextConfiguration> {

    private final TableEntryMapper tableEntryMapper = new TableEntryMapper();
    private final ValidTenantValueConstraintConfigurationMapper validTenantValueConstraintConfigurationMapper = new ValidTenantValueConstraintConfigurationMapper();
    private final SqlDefinitionsValidationMapper sqlDefinitionsValidationMapper = new SqlDefinitionsValidationMapper();

    @Override
    public SharedSchemaContextConfiguration map(com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration input) {
        return input == null ? null : new SharedSchemaContextConfiguration()
                .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(input.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables())
                .setCurrentTenantIdProperty(input.getCurrentTenantIdProperty() == null ? null : valueOf(input.getCurrentTenantIdProperty()))
                .setCurrentTenantIdPropertyType(input.getCurrentTenantIdPropertyType() == null ? null : valueOf(input.getCurrentTenantIdPropertyType()))
                .setDefaultSchema(input.getDefaultSchema())
                .setDefaultTenantIdColumn(input.getDefaultTenantIdColumn() == null ? null : valueOf(input.getDefaultTenantIdColumn()))
                .setEqualsCurrentTenantIdentifierFunctionName(input.getEqualsCurrentTenantIdentifierFunctionName() == null ? null : valueOf(input.getEqualsCurrentTenantIdentifierFunctionName()))
                .setForceRowLevelSecurityForTableOwner(input.getForceRowLevelSecurityForTableOwner())
                .setGetCurrentTenantIdFunctionName(input.getGetCurrentTenantIdFunctionName() == null ? null : valueOf(input.getGetCurrentTenantIdFunctionName()))
                .setGrantee(input.getGrantee())
                .setSetCurrentTenantIdFunctionName(input.getSetCurrentTenantIdFunctionName() == null ? null : valueOf(input.getSetCurrentTenantIdFunctionName()))
                .setTenantHasAuthoritiesFunctionName(input.getTenantHasAuthoritiesFunctionName() == null ? null : valueOf(input.getTenantHasAuthoritiesFunctionName()))
                .setTables(input.getTables() == null ? null : input.getTables().stream().map(tableEntry -> tableEntryMapper.map(tableEntry)).collect(toList()))
                .setValidTenantValueConstraint(validTenantValueConstraintConfigurationMapper.map(input.getValidTenantValueConstraint()))
                .setSqlDefinitionsValidation(sqlDefinitionsValidationMapper.map(input.getSqlDefinitionsValidation()));
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration unmap(SharedSchemaContextConfiguration output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration()
                .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(output.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables())
                .setCurrentTenantIdProperty(output.getCurrentTenantIdProperty() == null ? null : output.getCurrentTenantIdProperty().getValue())
                .setCurrentTenantIdPropertyType(output.getCurrentTenantIdPropertyType() == null ? null : output.getCurrentTenantIdPropertyType().getValue())
                .setDefaultSchema(output.getDefaultSchema())
                .setDefaultTenantIdColumn(output.getDefaultTenantIdColumn() == null ? null : output.getDefaultTenantIdColumn().getValue())
                .setEqualsCurrentTenantIdentifierFunctionName(output.getEqualsCurrentTenantIdentifierFunctionName() == null ? null : output.getEqualsCurrentTenantIdentifierFunctionName().getValue())
                .setForceRowLevelSecurityForTableOwner(output.getForceRowLevelSecurityForTableOwner())
                .setGetCurrentTenantIdFunctionName(output.getGetCurrentTenantIdFunctionName() == null ? null : output.getGetCurrentTenantIdFunctionName().getValue())
                .setGrantee(output.getGrantee())
                .setSetCurrentTenantIdFunctionName(output.getSetCurrentTenantIdFunctionName() == null ? null : output.getSetCurrentTenantIdFunctionName().getValue())
                .setTenantHasAuthoritiesFunctionName(output.getTenantHasAuthoritiesFunctionName() == null ? null : output.getTenantHasAuthoritiesFunctionName().getValue())
                .setTables(output.getTables() == null ? null : output.getTables().stream().map(tableEntry -> tableEntryMapper.unmap(tableEntry)).collect(toList()))
                .setValidTenantValueConstraint(validTenantValueConstraintConfigurationMapper.unmap(output.getValidTenantValueConstraint()))
                .setSqlDefinitionsValidation(sqlDefinitionsValidationMapper.unmap(output.getSqlDefinitionsValidation()));
    }
}
