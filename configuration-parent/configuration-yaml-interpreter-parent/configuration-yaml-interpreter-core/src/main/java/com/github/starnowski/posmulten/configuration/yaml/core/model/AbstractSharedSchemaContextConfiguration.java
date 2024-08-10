/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.configuration.yaml.core.model;


import java.util.List;

public interface AbstractSharedSchemaContextConfiguration<T extends AbstractSharedSchemaContextConfiguration, SWWNBV extends AbstractStringWrapperWithNotBlankValue, CDE extends AbstractCustomDefinitionEntry, VTVCC extends AbstractValidTenantValueConstraintConfiguration, TE extends AbstractTableEntry, SDV extends AbstractSqlDefinitionsValidation> {

    String getDefaultSchema();

    void setDefaultSchema(String defaultSchema);

    SWWNBV getCurrentTenantIdPropertyType();

    SWWNBV getCurrentTenantIdProperty();

    SWWNBV getGetCurrentTenantIdFunctionName();

    SWWNBV getSetCurrentTenantIdFunctionName();

    SWWNBV getEqualsCurrentTenantIdentifierFunctionName();

    SWWNBV getTenantHasAuthoritiesFunctionName();

    Boolean getForceRowLevelSecurityForTableOwner();

    void setForceRowLevelSecurityForTableOwner(Boolean forceRowLevelSecurityForTableOwner);

    SWWNBV getDefaultTenantIdColumn();

    String getGrantee();

    void setGrantee(String grantee);

    Boolean getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables();

    void setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(Boolean currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables);

    VTVCC getValidTenantValueConstraint();

    void setValidTenantValueConstraint(VTVCC validTenantValueConstraint);

    List<TE> getTables();

    void setTables(List<TE> tables);

    SDV getSqlDefinitionsValidation();

    void setSqlDefinitionsValidation(SDV sqlDefinitionsValidation);

    List<CDE> getCustomSQLDefinitions();

    void setCustomSQLDefinitions(List<CDE> customSQLDefinitions);

    Boolean getCreateForeignKeyConstraintWithTenantColumn();
    T setCreateForeignKeyConstraintWithTenantColumn(Boolean createForeignKeyConstraintWithTenantColumn);

    T setCurrentTenantIdPropertyType(String currentTenantIdPropertyType);

    T setCurrentTenantIdProperty(String currentTenantIdProperty);

    T setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName);

    T setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName);

    T setEqualsCurrentTenantIdentifierFunctionName(String equalsCurrentTenantIdentifierFunctionName);

    T setDefaultTenantIdColumn(String defaultTenantIdColumn);

    T setCurrentTenantIdPropertyType(SWWNBV currentTenantIdPropertyType);

    T setCurrentTenantIdProperty(SWWNBV currentTenantIdProperty);

    T setGetCurrentTenantIdFunctionName(SWWNBV getCurrentTenantIdFunctionName);

    T setSetCurrentTenantIdFunctionName(SWWNBV setCurrentTenantIdFunctionName);

    T setEqualsCurrentTenantIdentifierFunctionName(SWWNBV equalsCurrentTenantIdentifierFunctionName);

    T setTenantHasAuthoritiesFunctionName(SWWNBV tenantHasAuthoritiesFunctionName);

    T setDefaultTenantIdColumn(SWWNBV defaultTenantIdColumn);

    T withCustomSQLDefinitions(List<CDE> customSQLDefinitions);
}
