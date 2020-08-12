package com.github.starnowski.posmulten.postgresql.core.context;

public class SharedSchemaContextRequest {

    private String defaultSchema;
    private String currentTenantIdProperty = "posmulten.tenant_Id";


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
