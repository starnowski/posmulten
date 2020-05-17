package com.github.starnowski.posmulten.postgresql.core.rls;

public interface RLSPolicyProducerParameters {

    String getPolicyName();

    String getPolicyTable();

    String getPolicySchema();

    //TODO grantee

    String getTenantIdColumn();

    PermissionCommandPolicyEnum getPermissionCommandPolicy();

    TenantHasAuthoritiesFunctionInvocationFactory getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory();

    TenantHasAuthoritiesFunctionInvocationFactory getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory();
}
