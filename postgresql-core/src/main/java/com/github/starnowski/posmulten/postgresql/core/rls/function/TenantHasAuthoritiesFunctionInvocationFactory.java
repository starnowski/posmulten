package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum;

public interface TenantHasAuthoritiesFunctionInvocationFactory {

    String returnTenantHasAuthoritiesFunctionInvocation(FunctionArgumentValue tenantIdValue, PermissionCommandPolicyEnum permissionCommandPolicy, RLSExpressionTypeEnum rlsExpressionType, FunctionArgumentValue table, FunctionArgumentValue schema);
}
