package com.github.starnowski.posmulten.postgresql.core.context;

public class SharedSchemaContextRequest {

    private String defaultSchema;
    private String currentTenantIdProperty = "posmulten.tenant_id";
    private String currentTenantIdPropertyType = "VARCHAR(255)";
    private String getCurrentTenantIdFunctionName;
    private String setCurrentTenantIdFunctionName;
    private String equalsCurrentTenantIdentifierFunctionName;
    private String tenantHasAuthoritiesFunctionName;

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
}
