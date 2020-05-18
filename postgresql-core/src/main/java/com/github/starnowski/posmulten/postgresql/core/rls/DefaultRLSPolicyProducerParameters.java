package com.github.starnowski.posmulten.postgresql.core.rls;

public class DefaultRLSPolicyProducerParameters implements RLSPolicyProducerParameters{

    private final String policyName;
    private final String policyTable;

    private final String policySchema;

    private final String grantee;
    private final String tenantIdColumn;
    private final PermissionCommandPolicyEnum permissionCommandPolicy;
    private final TenantHasAuthoritiesFunctionInvocationFactory withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    private final TenantHasAuthoritiesFunctionInvocationFactory usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;

    public DefaultRLSPolicyProducerParameters(String policyName, String policyTable, String policySchema, String grantee, String tenantIdColumn, PermissionCommandPolicyEnum permissionCommandPolicy, TenantHasAuthoritiesFunctionInvocationFactory withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory, TenantHasAuthoritiesFunctionInvocationFactory usingExpressionTenantHasAuthoritiesFunctionInvocationFactory) {
        this.policyName = policyName;
        this.policyTable = policyTable;
        this.policySchema = policySchema;
        this.grantee = grantee;
        this.tenantIdColumn = tenantIdColumn;
        this.permissionCommandPolicy = permissionCommandPolicy;
        this.withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory = withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
        this.usingExpressionTenantHasAuthoritiesFunctionInvocationFactory = usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    }

    @Override
    public String getPolicyName() {
        return policyName;
    }

    @Override
    public String getPolicyTable() {
        return policyTable;
    }

    @Override
    public String getPolicySchema() {
        return policySchema;
    }

    @Override
    public String getGrantee() {
        return grantee;
    }

    @Override
    public String getTenantIdColumn() {
        return tenantIdColumn;
    }

    @Override
    public PermissionCommandPolicyEnum getPermissionCommandPolicy() {
        return permissionCommandPolicy;
    }

    @Override
    public TenantHasAuthoritiesFunctionInvocationFactory getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory() {
        return withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    }

    @Override
    public TenantHasAuthoritiesFunctionInvocationFactory getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory() {
        return usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    }
}
