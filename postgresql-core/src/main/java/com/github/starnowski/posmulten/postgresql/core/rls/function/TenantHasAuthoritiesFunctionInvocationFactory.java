package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicySupplier;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeSupplier;

public interface TenantHasAuthoritiesFunctionInvocationFactory {

    String returnTenantHasAuthoritiesFunctionInvocation(FunctionArgumentValue tenantIdValue, PermissionCommandPolicySupplier permissionCommandPolicy, RLSExpressionTypeSupplier rlsExpressionType, FunctionArgumentValue table, FunctionArgumentValue schema);
}
