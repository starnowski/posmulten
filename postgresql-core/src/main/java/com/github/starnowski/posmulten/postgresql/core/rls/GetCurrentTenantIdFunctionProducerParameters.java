package com.github.starnowski.posmulten.postgresql.core.rls;

public class GetCurrentTenantIdFunctionProducerParameters implements IGetCurrentTenantIdFunctionProducerParameters{

    private final String functionName;
    private final String currentTenantIdProperty;
    private final String schema;
    private final String functionReturnType;

    public GetCurrentTenantIdFunctionProducerParameters(String functionName, String currentTenantIdProperty, String schema, String functionReturnType) {
        this.functionName = functionName;
        this.currentTenantIdProperty = currentTenantIdProperty;
        this.schema = schema;
        this.functionReturnType = functionReturnType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getCurrentTenantIdProperty() {
        return currentTenantIdProperty;
    }

    public String getSchema() {
        return schema;
    }

    public String getFunctionReturnType() {
        return functionReturnType;
    }
}
