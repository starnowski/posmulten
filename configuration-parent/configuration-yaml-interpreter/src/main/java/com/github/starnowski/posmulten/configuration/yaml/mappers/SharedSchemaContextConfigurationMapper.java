/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

import static com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue.valueOf;
import static java.util.stream.Collectors.toList;

public class SharedSchemaContextConfigurationMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration, SharedSchemaContextConfiguration> {

    private final TableEntryMapper tableEntryMapper = new TableEntryMapper();
    private final ValidTenantValueConstraintConfigurationMapper validTenantValueConstraintConfigurationMapper = new ValidTenantValueConstraintConfigurationMapper();
    private final SqlDefinitionsValidationMapper sqlDefinitionsValidationMapper = new SqlDefinitionsValidationMapper();
    private final CustomDefinitionEntryMapper customDefinitionEntryMapper = new CustomDefinitionEntryMapper();

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
                .setSqlDefinitionsValidation(sqlDefinitionsValidationMapper.map(input.getSqlDefinitionsValidation()))
                .setCustomSQLDefinitions(input.getCustomDefinitions() == null ? null : input.getCustomDefinitions().stream().map(customDefinitionEntry -> customDefinitionEntryMapper.map(customDefinitionEntry)).collect(toList()));
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
                .setSqlDefinitionsValidation(sqlDefinitionsValidationMapper.unmap(output.getSqlDefinitionsValidation()))
                .setCustomDefinitions(output.getCustomSQLDefinitions() == null ? null : output.getCustomSQLDefinitions().stream().map(customDefinitionEntry -> customDefinitionEntryMapper.unmap(customDefinitionEntry)).collect(toList()));
    }
}
