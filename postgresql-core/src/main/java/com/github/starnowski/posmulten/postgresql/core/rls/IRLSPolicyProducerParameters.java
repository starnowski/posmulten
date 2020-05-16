package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionInvocationFactory;

public interface IRLSPolicyProducerParameters {

    String getPolicyName();

    String getPolicyTabe();

    String getPolicySchema();

    String getTenantIdColumn();

    RLSExpressionTypeEnum getRlsExpressionType();

    PermissionCommandPolicyEnum getPermissionCommandPolicy();

    TenantHasAuthoritiesFunctionInvocationFactory getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory();

    TenantHasAuthoritiesFunctionInvocationFactory getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory();
}
