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
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.context.validators.*;
import com.github.starnowski.posmulten.postgresql.core.context.validators.factories.IdentifierLengthValidatorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * The builder component responsible for creation of object of type {@link ISharedSchemaContext}.
 * Component create result object based on property {@link #sharedSchemaContextRequest}.
 * For setting values of results project the builder component use the enricher components of type {@link ISharedSchemaContextEnricher},
 * specified in {@link #enrichers} collection.
 */
public class DefaultSharedSchemaContextBuilder {

    /**
     * Collection that stores objects of type {@link ISharedSchemaContextEnricher} used for enriching result object ({@link #build()} method).
     */
    private List<ISharedSchemaContextEnricher> enrichers = asList(new GetCurrentTenantIdFunctionDefinitionEnricher(), new SetCurrentTenantIdFunctionDefinitionEnricher(), new TenantHasAuthoritiesFunctionDefinitionEnricher(), new IsTenantValidFunctionInvocationFactoryEnricher(), new TenantColumnSQLDefinitionsEnricher(), new TableRLSSettingsSQLDefinitionsEnricher(), new TableRLSPolicyEnricher(), new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(), new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(), new IsTenantIdentifierValidConstraintEnricher(), new DefaultValueForTenantColumnEnricher());

    /**
     * Collection that stores objects of type {@link ISharedSchemaContextRequestValidator} used for validation of request object (type {@link SharedSchemaContextRequest}) in {@link #build()} method.
     */
    private List<ISharedSchemaContextRequestValidator> validators = asList(new ForeignKeysMappingSharedSchemaContextRequestValidator(), new CreateTenantColumnTableMappingSharedSchemaContextRequestValidator(), new TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidator());

    /**
     * Collection that stores objects of type {@link ISQLDefinitionsValidator} used for validation of generated SQL definitions ({@link #build()} method).
     */
    private List<ISQLDefinitionsValidator> sqlDefinitionsValidators = new ArrayList<>();

    private boolean disableDefaultSqlDefinitionsValidators = false;

    private final SharedSchemaContextRequest sharedSchemaContextRequest = new SharedSchemaContextRequest();

    /**
     * Constructor set value null for the default schema
     */
    public DefaultSharedSchemaContextBuilder() {
        this(null);
    }

    /**
     * Constructor that ables to specify the default schema
     * @param defaultSchema name of default schema used during building process
     */
    public DefaultSharedSchemaContextBuilder(String defaultSchema) {
        this.sharedSchemaContextRequest.setDefaultSchema(defaultSchema);
    }

    /**
     * Builds shared schema context based on properties {@link SharedSchemaContextRequest#defaultSchema} and {@link #sharedSchemaContextRequest}.
     * Context is enricher in the loop by each enricher from {@link #enrichers}  collection by an order which they were
     * added into the collection.
     * Before enriching the result object the request object is validated by all validators stored in the {@link #validators} collection.
     * @return object of type {@link ISharedSchemaContext}
     * @throws SharedSchemaContextBuilderException exceptions thrown by enrichers and validators
     */
    public ISharedSchemaContext build() throws SharedSchemaContextBuilderException
    {
        ISharedSchemaContext context = new SharedSchemaContext();
        SharedSchemaContextRequest sharedSchemaContextRequestCopy = getSharedSchemaContextRequestCopy();
        List<ISharedSchemaContextRequestValidator>validators = getValidatorsCopy();
        for (ISharedSchemaContextRequestValidator validator : validators)
        {
            SharedSchemaContextRequest request = getSharedSchemaContextRequestCopyOrNull(sharedSchemaContextRequestCopy);
            validator.validate(request);
        }
        List<ISharedSchemaContextEnricher> enrichers  = getEnrichersCopy();
        for (ISharedSchemaContextEnricher enricher : enrichers)
        {
            SharedSchemaContextRequest request = getSharedSchemaContextRequestCopyOrNull(sharedSchemaContextRequestCopy);
            context = enricher.enrich(context, request);
        }
        List<ISQLDefinitionsValidator> sqlDefinitionsValidators = prepareSqlDefinitionsValidators(sharedSchemaContextRequestCopy);
        for (ISQLDefinitionsValidator validator : sqlDefinitionsValidators)
        {
            //TODO Add tests
            validator.validate(context.getSqlDefinitions());
        }
        return context;
    }

    /**
     *
     * @return copy of the {@link #enrichers} collection
     */
    public List<ISharedSchemaContextEnricher> getEnrichersCopy() {
        return enrichers == null ? new ArrayList<>() : new ArrayList<>(enrichers);
    }

    /**
     *
     * @return copy of the {@link #validators} collection
     */
    public List<ISharedSchemaContextRequestValidator> getValidatorsCopy() {
        return validators == null ? new ArrayList<>() : new ArrayList<>(validators);
    }

    /**
     * Setting the {@link #enrichers} collection
     * @param enrichers new enrichers lists
     * @return builder object for which method was invoked
     */
    public DefaultSharedSchemaContextBuilder setEnrichers(List<ISharedSchemaContextEnricher> enrichers) {
        this.enrichers = enrichers;
        return this;
    }

    /**
     *
     * @return copy of the {@link #sharedSchemaContextRequest} property
     */
    public SharedSchemaContextRequest getSharedSchemaContextRequestCopy() {
        return getSharedSchemaContextRequestCopyOrNull(sharedSchemaContextRequest);
    }

    /**
     * Setting the type of column that stores the tenant identifier
     * @param currentTenantIdPropertyType type of column that stores the tenant identifier
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#currentTenantIdPropertyType
     * @see GetCurrentTenantIdFunctionDefinitionEnricher
     * @see SetCurrentTenantIdFunctionDefinitionEnricher
     * @see TenantColumnSQLDefinitionsEnricher
     * @see TenantHasAuthoritiesFunctionDefinitionEnricher
     */
    public DefaultSharedSchemaContextBuilder setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        sharedSchemaContextRequest.setCurrentTenantIdPropertyType(currentTenantIdPropertyType);
        return this;
    }

    /**
     * Setting the name of the property that stores the current tenant identifier
     * @param currentTenantIdProperty name of the property that stores the current tenant identifier
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#currentTenantIdProperty
     * @see GetCurrentTenantIdFunctionDefinitionEnricher
     * @see SetCurrentTenantIdFunctionDefinitionEnricher
     * @see TenantColumnSQLDefinitionsEnricher
     */
    public DefaultSharedSchemaContextBuilder setCurrentTenantIdProperty(String currentTenantIdProperty) {
        sharedSchemaContextRequest.setCurrentTenantIdProperty(currentTenantIdProperty);
        return this;
    }

    /**
     * Setting the name of the function that returns current tenant identifier
     * @param getCurrentTenantIdFunctionName name of function that returns current tenant identifier
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#getCurrentTenantIdFunctionName
     * @see GetCurrentTenantIdFunctionDefinitionEnricher
     */
    public DefaultSharedSchemaContextBuilder setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setGetCurrentTenantIdFunctionName(getCurrentTenantIdFunctionName);
        return this;
    }

    /**
     * Setting the name of the function that set current tenant identifier
     * @param setCurrentTenantIdFunctionName name of the function that set current tenant identifier
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#setCurrentTenantIdFunctionName
     * @see SetCurrentTenantIdFunctionDefinitionEnricher
     */
    public DefaultSharedSchemaContextBuilder setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setSetCurrentTenantIdFunctionName(setCurrentTenantIdFunctionName);
        return this;
    }

    /**
     * Setting the name of the function that checks if passed identifier is equal to the current tenant identifier
     * @param equalsCurrentTenantIdentifierFunctionName name of the function that checks if passed identifier is equal to the current tenant identifier
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#equalsCurrentTenantIdentifierFunctionName
     * @see TenantHasAuthoritiesFunctionDefinitionEnricher
     */
    public DefaultSharedSchemaContextBuilder setEqualsCurrentTenantIdentifierFunctionName(String equalsCurrentTenantIdentifierFunctionName) {
        sharedSchemaContextRequest.setEqualsCurrentTenantIdentifierFunctionName(equalsCurrentTenantIdentifierFunctionName);
        return this;
    }

    /**
     * Setting the name of the function that checks if the current tenant is allowed to process database table row.
     * @param tenantHasAuthoritiesFunctionName name of the function that checks if the current tenant is allowed to process database table row
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#tenantHasAuthoritiesFunctionName
     * @see TenantHasAuthoritiesFunctionDefinitionEnricher
     */
    public DefaultSharedSchemaContextBuilder setTenantHasAuthoritiesFunctionName(String tenantHasAuthoritiesFunctionName) {
        sharedSchemaContextRequest.setTenantHasAuthoritiesFunctionName(tenantHasAuthoritiesFunctionName);
        return this;
    }

    /**
     * Marking specific table from defined default schema for builder ({@link SharedSchemaContextRequest#defaultSchema}) as table where a column for tenant identifier should be added.
     * @param table name of table where a column for tenant identifier should be added.
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#createTenantColumnTableLists
     * @see TenantColumnSQLDefinitionsEnricher
     */
    public DefaultSharedSchemaContextBuilder createTenantColumnForTable(String table)
    {
        TableKey tableKey = new TableKey(table, sharedSchemaContextRequest.getDefaultSchema());
        sharedSchemaContextRequest.getCreateTenantColumnTableLists().add(tableKey);
        return this;
    }

    /**
     * Register table that should have create row level security policy.
     * Table belongs to defined default schema for builder ({@link SharedSchemaContextRequest#defaultSchema}).
     * @param table name of table
     * @param primaryKeyColumnsList map of primary key columns and their types in table. Column name is the map key and column type is its value
     * @param tenantColumnName name of column that stores tenant identifier in table
     * @param rlsPolicyName name of row level security policy
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#tableColumnsList
     * @see SharedSchemaContextRequest#tableRLSPolicies
     * @see TableRLSPolicyEnricher
     * @see TableRLSSettingsSQLDefinitionsEnricher
     */
    public DefaultSharedSchemaContextBuilder createRLSPolicyForTable(String table, Map<String, String> primaryKeyColumnsList, String tenantColumnName, String rlsPolicyName)
    {
        TableKey tableKey = new TableKey(table, sharedSchemaContextRequest.getDefaultSchema());
        sharedSchemaContextRequest.getTableColumnsList().put(tableKey, new DefaultTableColumns(tenantColumnName, primaryKeyColumnsList));
        sharedSchemaContextRequest.getTableRLSPolicies().put(tableKey, new DefaultTableRLSPolicyProperties(rlsPolicyName));
        return this;
    }

    /**
     * Setting if builder should <a href="https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html">force row level security for table owner</a>.
     * By default, the builder does not do this.
     * @param forceRowLevelSecurityForTableOwner true if builder should force row level security for table owner
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#forceRowLevelSecurityForTableOwner
     * @see TableRLSSettingsSQLDefinitionsEnricher
     */
    public DefaultSharedSchemaContextBuilder setForceRowLevelSecurityForTableOwner(boolean forceRowLevelSecurityForTableOwner) {
        sharedSchemaContextRequest.setForceRowLevelSecurityForTableOwner(forceRowLevelSecurityForTableOwner);
        return this;
    }

    /**
     * Setting the default name for the column that stores the tenant identifier for table row.
     * @param defaultTenantIdColumn name for column that stores the tenant identifier for table row
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#defaultTenantIdColumn
     * @see TableRLSPolicyEnricher
     */
    public DefaultSharedSchemaContextBuilder setDefaultTenantIdColumn(String defaultTenantIdColumn) {
        sharedSchemaContextRequest.setDefaultTenantIdColumn(defaultTenantIdColumn);
        return this;
    }

    /**
     * Setting the default grantee for which the row level security should be added.
     * @param grantee grantee for which the row level security should be added
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#grantee
     * @see TableRLSPolicyEnricher
     */
    public DefaultSharedSchemaContextBuilder setGrantee(String grantee) {
        sharedSchemaContextRequest.setGrantee(grantee);
        return this;
    }

    /**
     * Register the request for creation of constraint that checks if foreign key in the main table refers to record
     * that exists in the foreign table and which belongs to the current tenant.
     * @param mainTable name of the main table that contains columns with foreign key
     * @param foreignKeyTable name of the foreign table
     * @param foreignKeyPrimaryKeyColumnsMappings map contains information about which foreign key column refers to specific primary key column. The foreign key column is the map key and the primary key column is its value.
     * @param constraintName constraint name
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#sameTenantConstraintForForeignKeyProperties
     * @see IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher
     */
    public DefaultSharedSchemaContextBuilder createSameTenantConstraintForForeignKey(String mainTable, String foreignKeyTable, Map<String, String> foreignKeyPrimaryKeyColumnsMappings, String constraintName) {
        sharedSchemaContextRequest.getSameTenantConstraintForForeignKeyProperties().put(new SameTenantConstraintForForeignKey(new TableKey(mainTable, sharedSchemaContextRequest.getDefaultSchema()), new TableKey(foreignKeyTable, sharedSchemaContextRequest.getDefaultSchema()), foreignKeyPrimaryKeyColumnsMappings.keySet()), new SameTenantConstraintForForeignKeyProperties(constraintName, foreignKeyPrimaryKeyColumnsMappings));
        return this;
    }

    /**
     * Setting the name for a function that checks if there is a record with a specified identifier that is assigned to
     * the current tenant for the specified table that exists in default schema  ({@link SharedSchemaContextRequest#defaultSchema}).
     * @param recordTable table name in default schema ({@link SharedSchemaContextRequest#defaultSchema})
     * @param functionName function name
     * @return builder object for which method was invoked
     * @see SharedSchemaContextRequest#functionThatChecksIfRecordExistsInTableNames
     */
    public DefaultSharedSchemaContextBuilder setNameForFunctionThatChecksIfRecordExistsInTable(String recordTable, String functionName) {
        sharedSchemaContextRequest.getFunctionThatChecksIfRecordExistsInTableNames().put(new TableKey(recordTable, sharedSchemaContextRequest.getDefaultSchema()), functionName);
        return this;
    }

    /**
     * Setting the {@link #validators} collection
     * @param validators new validators lists
     * @return builder object for which method was invoked
     */
    public DefaultSharedSchemaContextBuilder setValidators(List<ISharedSchemaContextRequestValidator> validators) {
        this.validators = validators;
        return this;
    }

    /**
     * Register the request for creation of constraints that are going to check if tenant column has valid value in all
     * tables that require rls policy.
     * @param tenantValuesBlacklist list of invalid tenant identifiers
     * @param isTenantValidFunctionName default name of function that check if tenant identifier is valid
     * @param isTenantValidConstraintName default name of constraint that check if tenant identifier is valid
     * @return builder object for which method was invoked
     */
    public DefaultSharedSchemaContextBuilder createValidTenantValueConstraint(List<String> tenantValuesBlacklist, String isTenantValidFunctionName, String isTenantValidConstraintName) {
        sharedSchemaContextRequest.setTenantValuesBlacklist(tenantValuesBlacklist);
        sharedSchemaContextRequest.setIsTenantValidFunctionName(isTenantValidFunctionName);
        sharedSchemaContextRequest.setIsTenantValidConstraintName(isTenantValidConstraintName);
        sharedSchemaContextRequest.setConstraintForValidTenantValueShouldBeAdded(true);
        return this;
    }

    /**
     * Register custom name for constraint that are going to check if tenant column has valid value in specified
     * table that require rls policy.
     * @param table table name
     * @param constraintName constraint name
     * @return builder object for which method was invoked
     */
    public DefaultSharedSchemaContextBuilder registerCustomValidTenantValueConstraintNameForTable(String table, String constraintName) {
        sharedSchemaContextRequest.getTenantValidConstraintCustomNamePerTables().put(new TableKey(table, sharedSchemaContextRequest.getDefaultSchema()), constraintName);
        return this;
    }

    /**
     * Setting if builder should add default value declaration for tenant column in all tables that required rls policy.
     * Default value is going to be current tenant identifier.
     * @param value true if builder should add default declaration
     * @return builder object for which method was invoked
     */
    public DefaultSharedSchemaContextBuilder setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(boolean value) {
        sharedSchemaContextRequest.setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(value);
        return this;
    }

    /**
     * Specify for which table the adding of default value declaration should be skipped.
     * @param value table name
     * @return builder object for which method was invoked
     * @see #setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(boolean)
     */
    public DefaultSharedSchemaContextBuilder skipAddingOfTenantColumnDefaultValueForTable(String value) {
        sharedSchemaContextRequest.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().add(new TableKey(value, sharedSchemaContextRequest.getDefaultSchema()));
        return this;
    }

    /**
     *
     * @return copy of the {@link #sqlDefinitionsValidators} collection
     */
    public List<ISQLDefinitionsValidator> getSqlDefinitionsValidatorsCopy() {
        return sqlDefinitionsValidators == null ? new ArrayList<>() : new ArrayList<>(sqlDefinitionsValidators);
    }

    protected List<ISQLDefinitionsValidator> prepareSqlDefinitionsValidators(SharedSchemaContextRequest request) throws InvalidSharedSchemaContextRequestException {
        if (this.disableDefaultSqlDefinitionsValidators) {
            return Arrays.asList(new FunctionDefinitionValidator(Arrays.asList((new IdentifierLengthValidatorFactory()).build(request))));
        }
        return getSqlDefinitionsValidatorsCopy();
    }

    /**
     * Setting the {@link #sqlDefinitionsValidators} collection
     * @param sqlDefinitionsValidators new validators lists
     * @return builder object for which method was invoked
     */
    public DefaultSharedSchemaContextBuilder setSqlDefinitionsValidators(List<ISQLDefinitionsValidator> sqlDefinitionsValidators) {
        this.sqlDefinitionsValidators = sqlDefinitionsValidators;
        this.disableDefaultSqlDefinitionsValidators = true;
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
