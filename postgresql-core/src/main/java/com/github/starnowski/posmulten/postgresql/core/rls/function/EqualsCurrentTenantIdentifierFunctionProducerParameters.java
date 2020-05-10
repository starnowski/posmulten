package com.github.starnowski.posmulten.postgresql.core.rls.function;

public class EqualsCurrentTenantIdentifierFunctionProducerParameters implements IEqualsCurrentTenantIdentifierFunctionProducerParameters {

    private final String functionName;
    private final String schema;
    private final String argumentType;
    private final IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory;

    public EqualsCurrentTenantIdentifierFunctionProducerParameters(String functionName, String schema, String argumentType, IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory) {
        this.functionName = functionName;
        this.schema = schema;
        this.argumentType = argumentType;
        this.getCurrentTenantIdFunctionInvocationFactory = getCurrentTenantIdFunctionInvocationFactory;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public String getArgumentType() {
        return argumentType;
    }

    @Override
    public IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory() {
        return getCurrentTenantIdFunctionInvocationFactory;
    }

}
