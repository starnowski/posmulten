package com.github.starnowski.posmulten.postgresql.core.rls.function;

public class TenantHasAuthoritiesFunctionProducerParameters implements ITenantHasAuthoritiesFunctionProducerParameters{

    private final String functionName;
    private final String schema;
    private final EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory; // TODO mandatory
    private final String tenantIdArgumentType;
    private final String permissionCommandPolicyArgumentType;
    private final String rlsExpressionArgumentType;
    private final String tableArgumentType;
    private final String schemaArgumentType;

    public TenantHasAuthoritiesFunctionProducerParameters(String functionName, String schema, EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory) {
        this(functionName, schema, equalsCurrentTenantIdentifierFunctionInvocationFactory, null, null, null, null, null);
    }

    public TenantHasAuthoritiesFunctionProducerParameters(String functionName, String schema, EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory, String tenantIdArgumentType, String permissionCommandPolicyArgumentType, String rlsExpressionArgumentType, String tableArgumentType, String schemaArgumentType) {
        this.functionName = functionName;
        this.schema = schema;
        this.equalsCurrentTenantIdentifierFunctionInvocationFactory = equalsCurrentTenantIdentifierFunctionInvocationFactory;
        this.tenantIdArgumentType = tenantIdArgumentType;
        this.permissionCommandPolicyArgumentType = permissionCommandPolicyArgumentType;
        this.rlsExpressionArgumentType = rlsExpressionArgumentType;
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

    public String getPermissionCommandPolicyArgumentType() {
        return permissionCommandPolicyArgumentType;
    }

    public String getRLSExpressionArgumentType() {
        return rlsExpressionArgumentType;
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
                ", permissionCommandPolicyArgumentType='" + permissionCommandPolicyArgumentType + '\'' +
                ", rlsExpressionArgumentType='" + rlsExpressionArgumentType + '\'' +
                ", tableArgumentType='" + tableArgumentType + '\'' +
                ", schemaArgumentType='" + schemaArgumentType + '\'' +
                ", equalsCurrentTenantIdentifierFunctionInvocationFactory=" + (equalsCurrentTenantIdentifierFunctionInvocationFactory == null ? null : equalsCurrentTenantIdentifierFunctionInvocationFactory.getClass()) +
                '}';
    }
}
