package com.github.starnowski.posmulten.postgresql.core.rls;

/*
 * TODO the USING and CHECK WITH expressions cannot be null in the same time
 */
public interface RLSPolicyProducerParameters {

    String getPolicyName();

    String getPolicyTable();

    String getPolicySchema();

    String getGrantee();

    String getTenantIdColumn();

    PermissionCommandPolicyEnum getPermissionCommandPolicy();

    TenantHasAuthoritiesFunctionInvocationFactory getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(); //TODO required for INSERT

    TenantHasAuthoritiesFunctionInvocationFactory getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(); //TODO required for SELECT and DELETE
}
