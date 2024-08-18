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
package com.github.starnowski.posmulten.configuration.yaml.core.mappers;



import com.github.starnowski.posmulten.configuration.yaml.core.model.*;

import static java.util.stream.Collectors.toList;

public abstract class AbstractSharedSchemaContextConfigurationMapper<PK extends AbstractPrimaryKeyDefinition<PK>,  FKC extends AbstractForeignKeyConfiguration<FKC>, TE extends AbstractTableEntry<RLSP, FKC, TE>, SWWNBV extends AbstractStringWrapperWithNotBlankValue, RLSP extends AbstractRLSPolicy<SWWNBV, PK, RLSP>, CDE extends AbstractCustomDefinitionEntry<CDE>, VTVCC extends AbstractValidTenantValueConstraintConfiguration<VTVCC, SWWNBV>, SDV extends AbstractSqlDefinitionsValidation<SDV>, T extends AbstractSharedSchemaContextConfiguration<SWWNBV, CDE, VTVCC, TE, SDV, T>> extends AbstractConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration, T> {


    protected abstract AbstractTableEntryMapper<SWWNBV, PK, RLSP, FKC , TE> getTableEntryMapper();

    protected abstract AbstractSqlDefinitionsValidationMapper<SDV> getSqlDefinitionsValidationMapper();

    protected abstract AbstractCustomDefinitionEntryMapper<CDE> getCustomDefinitionEntryMapper();
    protected abstract AbstractValidTenantValueConstraintConfigurationMapper<SWWNBV, VTVCC> getValidTenantValueConstraintConfigurationMapper();

    @Override
    public T map(com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration input) {
        return input == null ? null : createNewInstanceOfOutput()
                .setCreateForeignKeyConstraintWithTenantColumn(input.getCreateForeignKeyConstraintWithTenantColumn())
                .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(input.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables())
                .setCurrentTenantIdProperty(input.getCurrentTenantIdProperty() == null ? null : createStringWrapperWithNotBlankValue(input.getCurrentTenantIdProperty()))
                .setCurrentTenantIdPropertyType(input.getCurrentTenantIdPropertyType() == null ? null : createStringWrapperWithNotBlankValue(input.getCurrentTenantIdPropertyType()))
                .setDefaultSchema(input.getDefaultSchema())
                .setDefaultTenantIdColumn(input.getDefaultTenantIdColumn() == null ? null : createStringWrapperWithNotBlankValue(input.getDefaultTenantIdColumn()))
                .setEqualsCurrentTenantIdentifierFunctionName(input.getEqualsCurrentTenantIdentifierFunctionName() == null ? null : createStringWrapperWithNotBlankValue(input.getEqualsCurrentTenantIdentifierFunctionName()))
                .setForceRowLevelSecurityForTableOwner(input.getForceRowLevelSecurityForTableOwner())
                .setGetCurrentTenantIdFunctionName(input.getGetCurrentTenantIdFunctionName() == null ? null : createStringWrapperWithNotBlankValue(input.getGetCurrentTenantIdFunctionName()))
                .setGrantee(input.getGrantee())
                .setSetCurrentTenantIdFunctionName(input.getSetCurrentTenantIdFunctionName() == null ? null : createStringWrapperWithNotBlankValue(input.getSetCurrentTenantIdFunctionName()))
                .setTenantHasAuthoritiesFunctionName(input.getTenantHasAuthoritiesFunctionName() == null ? null : createStringWrapperWithNotBlankValue(input.getTenantHasAuthoritiesFunctionName()))
                .setTables(input.getTables() == null ? null : input.getTables().stream().map(tableEntry -> getTableEntryMapper().map(tableEntry)).collect(toList()))
                .setValidTenantValueConstraint(getValidTenantValueConstraintConfigurationMapper().map(input.getValidTenantValueConstraint()))
                .setSqlDefinitionsValidation(getSqlDefinitionsValidationMapper().map(input.getSqlDefinitionsValidation()))
                .setCustomSQLDefinitions(input.getCustomDefinitions() == null ? null : input.getCustomDefinitions().stream().map(customDefinitionEntry -> getCustomDefinitionEntryMapper().map(customDefinitionEntry)).collect(toList()));
    }

    protected abstract SWWNBV createStringWrapperWithNotBlankValue(String value);

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration unmap(T output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration()
                .setCreateForeignKeyConstraintWithTenantColumn(output.getCreateForeignKeyConstraintWithTenantColumn())
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
                .setTables(output.getTables() == null ? null : output.getTables().stream().map(tableEntry -> getTableEntryMapper().unmap(tableEntry)).collect(toList()))
                .setValidTenantValueConstraint(getValidTenantValueConstraintConfigurationMapper().unmap(output.getValidTenantValueConstraint()))
                .setSqlDefinitionsValidation(getSqlDefinitionsValidationMapper().unmap(output.getSqlDefinitionsValidation()))
                .setCustomDefinitions(output.getCustomSQLDefinitions() == null ? null : output.getCustomSQLDefinitions().stream().map(customDefinitionEntry -> getCustomDefinitionEntryMapper().unmap(customDefinitionEntry)).collect(toList()));
    }
}
