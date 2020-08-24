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
package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.enrichers.*;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * The builder component responsible for creation of object of type {@link AbstractSharedSchemaContext}.
 * Component create result object based on properties {@link #defaultSchema} and {@link #sharedSchemaContextRequest}.
 * For setting values of results project the builder component use the enricher components of type {@link AbstractSharedSchemaContextEnricher},
 * specified in {@link #enrichers} collection.
 */
public class DefaultSharedSchemaContextBuilder {

    private final String defaultSchema;
    private List<AbstractSharedSchemaContextEnricher> enrichers = asList(new GetCurrentTenantIdFunctionDefinitionEnricher(), new SetCurrentTenantIdFunctionDefinitionEnricher(), new TenantHasAuthoritiesFunctionDefinitionEnricher(), new TenantColumnSQLDefinitionsEnricher(), new TableRLSSettingsSQLDefinitionsEnricher(), new TableRLSPolicyEnricher(), new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(), new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher());
    private final SharedSchemaContextRequest sharedSchemaContextRequest = new SharedSchemaContextRequest();

    public DefaultSharedSchemaContextBuilder(String defaultSchema) {
        this.defaultSchema = defaultSchema;
        this.sharedSchemaContextRequest.setDefaultSchema(defaultSchema);
    }

    public AbstractSharedSchemaContext build() throws SharedSchemaContextBuilderException
    {
        AbstractSharedSchemaContext context = new SharedSchemaContext();
        List<AbstractSharedSchemaContextEnricher> enrichers  = getEnrichers();
        SharedSchemaContextRequest sharedSchemaContextRequestCopy = getSharedSchemaContextRequestCopy();
        for (AbstractSharedSchemaContextEnricher enricher : enrichers)
        {
            SharedSchemaContextRequest request = getSharedSchemaContextRequestCopyOrNull(sharedSchemaContextRequestCopy);
            context = enricher.enrich(context, request);
        }
        return context;
    }

    public List<AbstractSharedSchemaContextEnricher> getEnrichers() {
        return enrichers == null ? new ArrayList<>() : new ArrayList<>(enrichers);
    }

    public DefaultSharedSchemaContextBuilder setEnrichers(List<AbstractSharedSchemaContextEnricher> enrichers) {
        this.enrichers = enrichers;
        return this;
    }

    public SharedSchemaContextRequest getSharedSchemaContextRequestCopy() {
        return getSharedSchemaContextRequestCopyOrNull(sharedSchemaContextRequest);
    }

    public DefaultSharedSchemaContextBuilder setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        sharedSchemaContextRequest.setCurrentTenantIdPropertyType(currentTenantIdPropertyType);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setCurrentTenantIdProperty(String currentTenantIdProperty) {
        sharedSchemaContextRequest.setCurrentTenantIdProperty(currentTenantIdProperty);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setGetCurrentTenantIdFunctionName(getCurrentTenantIdFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setSetCurrentTenantIdFunctionName(setCurrentTenantIdFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setEqualsCurrentTenantIdentifierFunctionName(String equalsCurrentTenantIdentifierFunctionName) {
        sharedSchemaContextRequest.setEqualsCurrentTenantIdentifierFunctionName(equalsCurrentTenantIdentifierFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setTenantHasAuthoritiesFunctionName(String tenantHasAuthoritiesFunctionName) {
        sharedSchemaContextRequest.setTenantHasAuthoritiesFunctionName(tenantHasAuthoritiesFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder createTenantColumnForTable(String table)
    {
        TableKey tableKey = new TableKey(table, this.defaultSchema);
        sharedSchemaContextRequest.getCreateTenantColumnTableLists().add(tableKey);
        return this;
    }

    public DefaultSharedSchemaContextBuilder createRLSPolicyForColumn(String table, Map<String, String> primaryKeyColumnsList, String tenantColumnName, String rlsPolicyName)
    {
        TableKey tableKey = new TableKey(table, this.defaultSchema);
        sharedSchemaContextRequest.getTableColumnsList().put(tableKey, new DefaultTableColumns(tenantColumnName, primaryKeyColumnsList));
        sharedSchemaContextRequest.getTableRLSPolicies().put(tableKey, new DefaultTableRLSPolicyProperties(rlsPolicyName));
        return this;
    }

    public DefaultSharedSchemaContextBuilder setForceRowLevelSecurityForTableOwner(boolean forceRowLevelSecurityForTableOwner) {
        sharedSchemaContextRequest.setForceRowLevelSecurityForTableOwner(forceRowLevelSecurityForTableOwner);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setDefaultTenantIdColumn(String defaultTenantIdColumn) {
        sharedSchemaContextRequest.setDefaultTenantIdColumn(defaultTenantIdColumn);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setGrantee(String grantee) {
        sharedSchemaContextRequest.setGrantee(grantee);
        return this;
    }

    public DefaultSharedSchemaContextBuilder createSameTenantConstraintForForeignKey(String mainTable, String foreignKeyTable, Map<String, String> foreignKeyPrimaryKeyColumnsMappings, String constraintName) {
        sharedSchemaContextRequest.getSameTenantConstraintForForeignKeyProperties().put(new SameTenantConstraintForForeignKey(new TableKey(mainTable, defaultSchema), new TableKey(foreignKeyTable, defaultSchema), foreignKeyPrimaryKeyColumnsMappings.keySet()), new SameTenantConstraintForForeignKeyProperties(constraintName, foreignKeyPrimaryKeyColumnsMappings));
        return this;
    }

    public DefaultSharedSchemaContextBuilder setNameForFunctionThatChecksIfRecordExistsInTable(String recordTable, String functionName) {
        sharedSchemaContextRequest.getFunctionThatChecksIfRecordExistsInTableNames().put(new TableKey(recordTable, defaultSchema), functionName);
        return this;
    }

    protected SharedSchemaContextRequest getSharedSchemaContextRequestCopyOrNull(SharedSchemaContextRequest request) {
        try {
            return (SharedSchemaContextRequest) request.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
