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
package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.*;

public class SharedSchemaContextRequest implements Cloneable {

    public static final String DEFAULT_TENANT_ID_COLUMN = "tenant_id";
    /**
     * A map that stores information about columns that are required to creation of shared schema multi-tenancy strategy.
     * Information about columns are store for each table that required to have row level security policy.
     * The table identifier ({@link TableKey}) is the map key and  information ({@link ITableColumns}) about columns are its value.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantColumnSQLDefinitionsEnricher
     */
    private final Map<TableKey, ITableColumns> tableColumnsList = new HashMap<>();
    /**
     * Collection that stores table identifiers ({@link TableKey}) for which a column for tenant identifier should be added.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantColumnSQLDefinitionsEnricher
     */
    private final Set<TableKey> createTenantColumnTableLists = new HashSet<>();
    /**
     * A map that stores information that describes the row level security policy properties for tables.
     * Properties are store for each table that required to have row level security policy.
     * The table identifier ({@link TableKey}) is the map key and the row level security policy properties ({@link ITableRLSPolicyProperties}) are its value.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher
     */
    private final Map<TableKey, ITableRLSPolicyProperties> tableRLSPolicies = new HashMap<>();
    /**
     * A map that stores information that are required to the creation of constraint that checks if foreign key in the main table refers to record
     * that exists in the foreign table and which belongs to the current tenant.
     * The map key is an object of type {@link SameTenantConstraintForForeignKey} that contains table keys ({@link TableKey})
     * for main table ({@link SameTenantConstraintForForeignKey#mainTable}) that has foreign key columns and the foreign
     * table ({@link SameTenantConstraintForForeignKey#foreignKeyTable}) which has primary key columns. The map key also
     * contains the set of column names for the foreign key ({@link SameTenantConstraintForForeignKey#foreignKeyColumns}).
     * The object of type {@link ISameTenantConstraintForForeignKeyProperties} is the map value.
     * <p>
     * In case when {@link #createForeignKeyConstraintWithTenantColumn} has true value then instead of function that checks
     * if specific reference key exist in scope of tenant, the foreign key constraint that tenant column is part of composite key is being created.
     *
     * In case when {@link #ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant} is true then creation of functions that checks if
     * record exists for specific tenant is ignored.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher
     */
    private final Map<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties> sameTenantConstraintForForeignKeyProperties = new HashMap<>();
    /**
     * A map that stores the names for a function that checks if there is a record with a specified identifier that is
     * assigned to the current tenant for the specified table. The map key is a table identifier ({@link TableKey}), and the
     * value is the function name.
     */
    private final Map<TableKey, String> functionThatChecksIfRecordExistsInTableNames = new HashMap<>();
    /**
     * A map that stores the custom names for a constraint that checks if the tenant identifier is valid.
     * The map key is a table identifier ({@link TableKey}), and the value is the constraint name.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantIdentifierValidConstraintEnricher
     */
    private final Map<TableKey, String> tenantValidConstraintCustomNamePerTables = new HashMap<>();
    /**
     * A set of table identifiers for which adding of default value declaration for tenant column should be skipped.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.DefaultValueForTenantColumnEnricher
     */
    private final Set<TableKey> tablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped = new HashSet<>();
    /**
     * Pairs of custom SQL definitions and their positions.
     */
    private final List<CustomSQLDefinitionPair> customSQLDefinitionPairs = new ArrayList<>();
    /**
     * Name of default schema used during building process.
     */
    private String defaultSchema;
    /**
     * Name of the property that stores the current tenant identifier.
     */
    private String currentTenantIdProperty = "posmulten.tenant_id";
    /**
     * Type of column that stores the tenant identifier.
     * Default value is "VARCHAR(255)".
     */
    private String currentTenantIdPropertyType = "VARCHAR(255)";
    /**
     * Name of the function that returns the current tenant identifier.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.GetCurrentTenantIdFunctionDefinitionEnricher
     */
    private String getCurrentTenantIdFunctionName;
    /**
     * Name of the function that set the current tenant identifier.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.SetCurrentTenantIdFunctionDefinitionEnricher
     */
    private String setCurrentTenantIdFunctionName;
    /**
     * Name of the function that checks if passed identifier is equal to the current tenant identifier.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantHasAuthoritiesFunctionDefinitionEnricher
     */
    private String equalsCurrentTenantIdentifierFunctionName;
    /**
     * Name of the function that checks if the current tenant is allowed to process database table row.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantHasAuthoritiesFunctionDefinitionEnricher
     */
    private String tenantHasAuthoritiesFunctionName;
    /**
     * Name for column that stores the tenant identifier for table row.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher
     */
    private String defaultTenantIdColumn = DEFAULT_TENANT_ID_COLUMN;
    /**
     * The toggle, based on which builder is going to <a href="https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html">force row level security for table owner</a>
     * (true) or not (false). The default value is false.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSSettingsSQLDefinitionsEnricher
     */
    private boolean forceRowLevelSecurityForTableOwner;
    /**
     * Default grantee for which the row level security should be added.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher
     */
    private String grantee;
    /**
     * A list that stores invalid tenant identifiers.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantValidFunctionInvocationFactoryEnricher
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantIdentifierValidConstraintEnricher
     */
    private List<String> tenantValuesBlacklist;
    /**
     * The name of function that check if passed tenant identifier is valid.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantValidFunctionInvocationFactoryEnricher
     */
    private String isTenantValidFunctionName;
    /**
     * The name of constraint that check if passed tenant identifier is valid.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantIdentifierValidConstraintEnricher
     */
    private String isTenantValidConstraintName;
    /**
     * The toggle, based on which builder is going to add constraint for all tables that required rls policy
     * (true) or not (false). The default value is false.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantIdentifierValidConstraintEnricher
     */
    private boolean constraintForValidTenantValueShouldBeAdded;
    /**
     * The toggle, based on which builder is going to add default value declaration for tenant column in all tables that required rls policy
     * (true) or not (false). The default value is false.
     *
     * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.DefaultValueForTenantColumnEnricher
     */
    private boolean currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables;
    /**
     * Maximum allowed length for the identifier.
     *
     * @see IdentifierLengthValidator
     */
    private Integer identifierMaxLength;
    /**
     * Minimum allowed length for the identifier.
     *
     * @see IdentifierLengthValidator
     */
    private Integer identifierMinLength;
    /**
     * Create the foreign key constraint that tenant column is part of composite key. Instead of function that check if
     * reference key exist for specific tenant.
     */
    private Boolean createForeignKeyConstraintWithTenantColumn;
    /**
     * Ignore creation of function that check if reference key exist for specific tenant.
     */
    private Boolean ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant;

    public Boolean getIgnoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant() {
        return ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant;
    }

    public void setIgnoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant(Boolean ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant) {
        this.ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant = ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant;
    }

    public Boolean getCreateForeignKeyConstraintWithTenantColumn() {
        return createForeignKeyConstraintWithTenantColumn;
    }

    public void setCreateForeignKeyConstraintWithTenantColumn(Boolean createForeignKeyConstraintWithTenantColumn) {
        this.createForeignKeyConstraintWithTenantColumn = createForeignKeyConstraintWithTenantColumn;
    }

    public String getDefaultTenantIdColumn() {
        return defaultTenantIdColumn;
    }

    public void setDefaultTenantIdColumn(String defaultTenantIdColumn) {
        this.defaultTenantIdColumn = defaultTenantIdColumn;
    }

    public String getEqualsCurrentTenantIdentifierFunctionName() {
        return equalsCurrentTenantIdentifierFunctionName;
    }

    public void setEqualsCurrentTenantIdentifierFunctionName(String equalsCurrentTenantIdentifierFunctionName) {
        this.equalsCurrentTenantIdentifierFunctionName = equalsCurrentTenantIdentifierFunctionName;
    }

    public String getTenantHasAuthoritiesFunctionName() {
        return tenantHasAuthoritiesFunctionName;
    }

    public void setTenantHasAuthoritiesFunctionName(String tenantHasAuthoritiesFunctionName) {
        this.tenantHasAuthoritiesFunctionName = tenantHasAuthoritiesFunctionName;
    }

    public String getSetCurrentTenantIdFunctionName() {
        return setCurrentTenantIdFunctionName;
    }

    public void setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName) {
        this.setCurrentTenantIdFunctionName = setCurrentTenantIdFunctionName;
    }

    public String getGetCurrentTenantIdFunctionName() {
        return getCurrentTenantIdFunctionName;
    }

    public void setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName) {
        this.getCurrentTenantIdFunctionName = getCurrentTenantIdFunctionName;
    }

    public String getCurrentTenantIdPropertyType() {
        return currentTenantIdPropertyType;
    }

    public void setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        this.currentTenantIdPropertyType = currentTenantIdPropertyType;
    }

    public String getCurrentTenantIdProperty() {
        return currentTenantIdProperty;
    }

    public void setCurrentTenantIdProperty(String currentTenantIdProperty) {
        this.currentTenantIdProperty = currentTenantIdProperty;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public Map<TableKey, ITableColumns> getTableColumnsList() {
        return tableColumnsList;
    }

    public Set<TableKey> getCreateTenantColumnTableLists() {
        return createTenantColumnTableLists;
    }

    public boolean isForceRowLevelSecurityForTableOwner() {
        return forceRowLevelSecurityForTableOwner;
    }

    public void setForceRowLevelSecurityForTableOwner(boolean forceRowLevelSecurityForTableOwner) {
        this.forceRowLevelSecurityForTableOwner = forceRowLevelSecurityForTableOwner;
    }

    public String getGrantee() {
        return grantee;
    }

    public void setGrantee(String grantee) {
        this.grantee = grantee;
    }

    public Map<TableKey, ITableRLSPolicyProperties> getTableRLSPolicies() {
        return tableRLSPolicies;
    }

    public Map<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties> getSameTenantConstraintForForeignKeyProperties() {
        return sameTenantConstraintForForeignKeyProperties;
    }

    public Map<TableKey, String> getFunctionThatChecksIfRecordExistsInTableNames() {
        return functionThatChecksIfRecordExistsInTableNames;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<String> getTenantValuesBlacklist() {
        return tenantValuesBlacklist;
    }

    public void setTenantValuesBlacklist(List<String> tenantValuesBlacklist) {
        this.tenantValuesBlacklist = tenantValuesBlacklist;
    }

    public String getIsTenantValidFunctionName() {
        return isTenantValidFunctionName;
    }

    public void setIsTenantValidFunctionName(String isTenantValidFunctionName) {
        this.isTenantValidFunctionName = isTenantValidFunctionName;
    }

    public String getIsTenantValidConstraintName() {
        return isTenantValidConstraintName;
    }

    public void setIsTenantValidConstraintName(String isTenantValidConstraintName) {
        this.isTenantValidConstraintName = isTenantValidConstraintName;
    }

    public boolean isConstraintForValidTenantValueShouldBeAdded() {
        return constraintForValidTenantValueShouldBeAdded;
    }

    public void setConstraintForValidTenantValueShouldBeAdded(boolean constraintForValidTenantValueShouldBeAdded) {
        this.constraintForValidTenantValueShouldBeAdded = constraintForValidTenantValueShouldBeAdded;
    }

    public Map<TableKey, String> getTenantValidConstraintCustomNamePerTables() {
        return tenantValidConstraintCustomNamePerTables;
    }

    public boolean isCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables() {
        return currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables;
    }

    public void setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(boolean value) {
        this.currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables = value;
    }

    public Set<TableKey> getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped() {
        return tablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped;
    }

    public Integer getIdentifierMaxLength() {
        return identifierMaxLength;
    }

    public void setIdentifierMaxLength(Integer identifierMaxLength) {
        this.identifierMaxLength = identifierMaxLength;
    }

    public Integer getIdentifierMinLength() {
        return identifierMinLength;
    }

    public void setIdentifierMinLength(Integer identifierMinLength) {
        this.identifierMinLength = identifierMinLength;
    }

    public List<CustomSQLDefinitionPair> getCustomSQLDefinitionPairs() {
        return customSQLDefinitionPairs;
    }

    /**
     * Resolves name for tenant column by table key ({@link TableKey}).
     * If there is no column name specified for the table to which the table key refers then the default column name is returned.
     *
     * @param tableKey - table key
     * @return null when table key is not registered otherwise tenant column name for the table
     */
    public String resolveTenantColumnByTableKey(TableKey tableKey) {
        ITableColumns tableColumns = getTableColumnsList().get(tableKey);
        if (tableColumns == null) {
            return null;
        }
        return tableColumns.getTenantColumnName() == null ? defaultTenantIdColumn : tableColumns.getTenantColumnName();
    }
}
