package com.github.starnowski.posmulten.postgresql.core.rls;

public interface IRLSPolicyProducerParameters {

    String getPolicyName();

    String getPolicyTable();

    String getPolicySchema();

    String getTenantIdColumn();

    RLSExpressionTypeEnum getRlsExpressionType();

    PermissionCommandPolicyEnum getPermissionCommandPolicy();

    TenantHasAuthoritiesFunctionInvocationFactory getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory();

    TenantHasAuthoritiesFunctionInvocationFactory getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory();
}
