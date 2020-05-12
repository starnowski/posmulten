package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicySupplier;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeSupplier;

public class TenantHasAuthoritiesFunctionDefinition extends DefaultFunctionDefinition implements TenantHasAuthoritiesFunctionInvocationFactory{

    public TenantHasAuthoritiesFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String returnTenantHasAuthoritiesFunctionInvocation(FunctionArgumentValue tenantIdValue, PermissionCommandPolicySupplier permissionCommandPolicy, RLSExpressionTypeSupplier rlsExpressionType, FunctionArgumentValue table, FunctionArgumentValue schema) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
