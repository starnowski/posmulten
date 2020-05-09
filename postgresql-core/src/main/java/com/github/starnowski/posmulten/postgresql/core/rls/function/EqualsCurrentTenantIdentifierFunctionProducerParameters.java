package com.github.starnowski.posmulten.postgresql.core.rls.function;

public class EqualsCurrentTenantIdentifierFunctionProducerParameters implements IEqualsCurrentTenantIdentifierFunctionProducerParameters {

    private final String functionName;
    private final String schema;
    private final String parameterType;
    private final String returnCurrentTenantIdStatement;
    private final IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory;

    public EqualsCurrentTenantIdentifierFunctionProducerParameters(String functionName, String schema, String parameterType, String returnCurrentTenantIdStatement, IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory) {
        this.functionName = functionName;
        this.schema = schema;
        this.parameterType = parameterType;
        this.returnCurrentTenantIdStatement = returnCurrentTenantIdStatement;
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
    public String getParameterType() {
        return parameterType;
    }

    @Override
    public String getReturnCurrentTenantIdStatement() {
        return returnCurrentTenantIdStatement;
    }

    @Override
    public IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory() {
        return getCurrentTenantIdFunctionInvocationFactory;
    }

}
