package com.github.starnowski.posmulten.postgresql.core.rls;

public class SetCurrentTenantIdFunctionProducerParameters implements ISetCurrentTenantIdFunctionProducerParameters{

    private final String functionName;
    private final String currentTenantIdProperty;
    private final String schema;
    private final String argumentType;

    public SetCurrentTenantIdFunctionProducerParameters(String functionName, String currentTenantIdProperty, String schema, String argumentType) {
        this.functionName = functionName;
        this.currentTenantIdProperty = currentTenantIdProperty;
        this.schema = schema;
        this.argumentType = argumentType;
    }

    @Override
    public String getArgumentType() {
        return argumentType;
    }

    @Override
    public String getCurrentTenantIdProperty() {
        return currentTenantIdProperty;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getSchema() {
        return schema;
    }
}
