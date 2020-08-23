package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedSchemaContextRequest implements Cloneable{

    private String defaultSchema;
    private String currentTenantIdProperty = "posmulten.tenant_id";
    private String currentTenantIdPropertyType = "VARCHAR(255)";
    private String getCurrentTenantIdFunctionName;
    private String setCurrentTenantIdFunctionName;
    private String equalsCurrentTenantIdentifierFunctionName;
    private String tenantHasAuthoritiesFunctionName;
    private String defaultTenantIdColumn = "tenant_id";
    private Map<TableKey, AbstractTableColumns> tableColumnsList = new HashMap<>();
    private Set<TableKey> createTenantColumnTableLists = new HashSet<>();
    private boolean forceRowLevelSecurityForTableOwner;
    private Map<TableKey, AbstractTableRLSPolicyProperties> tableRLSPolicies = new HashMap<>();
    private Map<SameTenantConstraintForForeignKey, SameTenantConstraintForForeignKeyProperties> sameTenantConstraintForForeignKeyProperties = new HashMap<>();
    private String grantee;
    private Map<TableKey, String> functionThatChecksIfRecordExistsInTableNames = new HashMap<>();

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

    public Map<TableKey, AbstractTableColumns> getTableColumnsList() {
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

    public Map<TableKey, AbstractTableRLSPolicyProperties> getTableRLSPolicies() {
        return tableRLSPolicies;
    }

    public Map<SameTenantConstraintForForeignKey, SameTenantConstraintForForeignKeyProperties> getSameTenantConstraintForForeignKeyProperties() {
        return sameTenantConstraintForForeignKeyProperties;
    }

    public Map<TableKey, String> getFunctionThatChecksIfRecordExistsInTableNames() {
        return functionThatChecksIfRecordExistsInTableNames;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
