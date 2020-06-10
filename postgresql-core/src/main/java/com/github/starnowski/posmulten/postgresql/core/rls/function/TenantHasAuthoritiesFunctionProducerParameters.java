package com.github.starnowski.posmulten.postgresql.core.rls.function;

public class TenantHasAuthoritiesFunctionProducerParameters implements ITenantHasAuthoritiesFunctionProducerParameters{

    private final String functionName;
    private final String schema;
    private final EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory;
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
                '}';
    }

    public static class TenantHasAuthoritiesFunctionProducerParametersBuilder
    {
        private String functionName;
        private String schema;
        private EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory;
        private String tenantIdArgumentType;
        private String permissionCommandPolicyArgumentType;
        private String rlsExpressionArgumentType;
        private String tableArgumentType;
        private String schemaArgumentType;

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withFunctionName(String functionName) {
            this.functionName = functionName;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withEqualsCurrentTenantIdentifierFunctionInvocationFactory(EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory) {
            this.equalsCurrentTenantIdentifierFunctionInvocationFactory = equalsCurrentTenantIdentifierFunctionInvocationFactory;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withTenantIdArgumentType(String tenantIdArgumentType) {
            this.tenantIdArgumentType = tenantIdArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withPermissionCommandPolicyArgumentType(String permissionCommandPolicyArgumentType) {
            this.permissionCommandPolicyArgumentType = permissionCommandPolicyArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withRlsExpressionArgumentType(String rlsExpressionArgumentType) {
            this.rlsExpressionArgumentType = rlsExpressionArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withTableArgumentType(String tableArgumentType) {
            this.tableArgumentType = tableArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withSchemaArgumentType(String schemaArgumentType) {
            this.schemaArgumentType = schemaArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParameters build()
        {
            return new TenantHasAuthoritiesFunctionProducerParameters(functionName, schema, equalsCurrentTenantIdentifierFunctionInvocationFactory, tenantIdArgumentType, permissionCommandPolicyArgumentType, rlsExpressionArgumentType, tableArgumentType, schemaArgumentType);
        }
    }
}
