package com.github.starnowski.posmulten.postgresql.core.rls.function;

public class TenantHasAuthoritiesFunctionProducerParameters implements ITenantHasAuthoritiesFunctionProducerParameters{

    private final String functionName;
    private final String schema;
    private final EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory; // TODO mandatory
    private final String tenantIdArgumentType;
    private final String usingExpressionArgumentType;
    private final String checkWithExpressionArgumentType;
    private final String tableArgumentType;
    private final String schemaArgumentType;

    public TenantHasAuthoritiesFunctionProducerParameters(String functionName, String schema, EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory) {
        this(functionName, schema, equalsCurrentTenantIdentifierFunctionInvocationFactory, null, null, null, null, null);
    }

    public TenantHasAuthoritiesFunctionProducerParameters(String functionName, String schema, EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory, String tenantIdArgumentType, String usingExpressionArgumentType, String checkWithExpressionArgumentType, String tableArgumentType, String schemaArgumentType) {
        this.functionName = functionName;
        this.schema = schema;
        this.equalsCurrentTenantIdentifierFunctionInvocationFactory = equalsCurrentTenantIdentifierFunctionInvocationFactory;
        this.tenantIdArgumentType = tenantIdArgumentType;
        this.usingExpressionArgumentType = usingExpressionArgumentType;
        this.checkWithExpressionArgumentType = checkWithExpressionArgumentType;
        this.tableArgumentType = tableArgumentType;
        this.schemaArgumentType = schemaArgumentType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getSchema() {
        return schema;
    }

    public String getTenantIdArgumentType() {
        return tenantIdArgumentType;
    }

    public String getUsingExpressionArgumentType() {
        return usingExpressionArgumentType;
    }

    public String getCheckWithExpressionArgumentType() {
        return checkWithExpressionArgumentType;
    }

    public String getTableArgumentType() {
        return tableArgumentType;
    }

    public String getSchemaArgumentType() {
        return schemaArgumentType;
    }

    public EqualsCurrentTenantIdentifierFunctionInvocationFactory getEqualsCurrentTenantIdentifierFunctionInvocationFactory() {
        return equalsCurrentTenantIdentifierFunctionInvocationFactory;
    }

    @Override
    public String toString() {
        return "TenantHasAuthoritiesFunctionProducerParameters{" +
                "functionName='" + functionName + '\'' +
                ", schema='" + schema + '\'' +
                ", tenantIdArgumentType='" + tenantIdArgumentType + '\'' +
                ", usingExpressionArgumentType='" + usingExpressionArgumentType + '\'' +
                ", checkWithExpressionArgumentType='" + checkWithExpressionArgumentType + '\'' +
                ", tableArgumentType='" + tableArgumentType + '\'' +
                ", schemaArgumentType='" + schemaArgumentType + '\'' +
                ", equalsCurrentTenantIdentifierFunctionInvocationFactory=" + (equalsCurrentTenantIdentifierFunctionInvocationFactory == null ? null : equalsCurrentTenantIdentifierFunctionInvocationFactory.getClass()) +
                '}';
    }
}
