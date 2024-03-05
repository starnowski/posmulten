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
package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class DefaultSharedSchemaContextBuilderConfigurationEnricher {

    private final TablesEntriesEnricher tablesEntriesEnricher;
    private final ValidTenantValueConstraintConfigurationEnricher validTenantValueConstraintConfigurationEnricher;
    private final SqlDefinitionsValidationEnricher sqlDefinitionsValidationEnricher;
    private final CustomDefinitionEntriesEnricher customDefinitionEntriesEnricher;

    public DefaultSharedSchemaContextBuilderConfigurationEnricher() {
        this(new TablesEntriesEnricher(), new ValidTenantValueConstraintConfigurationEnricher(), new SqlDefinitionsValidationEnricher(), new CustomDefinitionEntriesEnricher());
    }

    public DefaultSharedSchemaContextBuilderConfigurationEnricher(TablesEntriesEnricher tablesEntriesEnricher, ValidTenantValueConstraintConfigurationEnricher validTenantValueConstraintConfigurationEnricher,
                                                                  SqlDefinitionsValidationEnricher sqlDefinitionsValidationEnricher, CustomDefinitionEntriesEnricher customDefinitionEntriesEnricher) {
        this.tablesEntriesEnricher = tablesEntriesEnricher;
        this.validTenantValueConstraintConfigurationEnricher = validTenantValueConstraintConfigurationEnricher;
        this.sqlDefinitionsValidationEnricher = sqlDefinitionsValidationEnricher;
        this.customDefinitionEntriesEnricher = customDefinitionEntriesEnricher;
    }

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, SharedSchemaContextConfiguration contextConfiguration) {
        if (contextConfiguration.getCurrentTenantIdPropertyType() != null) {
            builder.setCurrentTenantIdPropertyType(contextConfiguration.getCurrentTenantIdPropertyType());
        }
        if (contextConfiguration.getCurrentTenantIdProperty() != null) {
            builder.setCurrentTenantIdProperty(contextConfiguration.getCurrentTenantIdProperty());
        }
        if (contextConfiguration.getGetCurrentTenantIdFunctionName() != null) {
            builder.setGetCurrentTenantIdFunctionName(contextConfiguration.getGetCurrentTenantIdFunctionName());
        }
        if (contextConfiguration.getSetCurrentTenantIdFunctionName() != null) {
            builder.setSetCurrentTenantIdFunctionName(contextConfiguration.getSetCurrentTenantIdFunctionName());
        }
        if (contextConfiguration.getEqualsCurrentTenantIdentifierFunctionName() != null) {
            builder.setEqualsCurrentTenantIdentifierFunctionName(contextConfiguration.getEqualsCurrentTenantIdentifierFunctionName());
        }
        if (contextConfiguration.getTenantHasAuthoritiesFunctionName() != null) {
            builder.setTenantHasAuthoritiesFunctionName(contextConfiguration.getTenantHasAuthoritiesFunctionName());
        }
        if (contextConfiguration.getForceRowLevelSecurityForTableOwner() != null) {
            builder.setForceRowLevelSecurityForTableOwner(contextConfiguration.getForceRowLevelSecurityForTableOwner());
        }
        if (contextConfiguration.getDefaultTenantIdColumn() != null) {
            builder.setDefaultTenantIdColumn(contextConfiguration.getDefaultTenantIdColumn());
        }
        if (contextConfiguration.getGrantee() != null) {
            builder.setGrantee(contextConfiguration.getGrantee());
        }
        if (contextConfiguration.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables() != null) {
            builder.setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(contextConfiguration.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables());
        }
        builder.setCreateForeignKeyConstraintWithTenantColumn(contextConfiguration.getCreateForeignKeyConstraintWithTenantColumn());
        validTenantValueConstraintConfigurationEnricher.enrich(builder, contextConfiguration.getValidTenantValueConstraint());
        tablesEntriesEnricher.enrich(builder, contextConfiguration.getTables());
        sqlDefinitionsValidationEnricher.enrich(builder, contextConfiguration.getSqlDefinitionsValidation());
        customDefinitionEntriesEnricher.enrich(builder, contextConfiguration.getCustomDefinitions());
        return builder;
    }
}
