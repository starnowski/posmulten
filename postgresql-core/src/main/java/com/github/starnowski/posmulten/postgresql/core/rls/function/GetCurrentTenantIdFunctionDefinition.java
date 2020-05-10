package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

public class GetCurrentTenantIdFunctionDefinition extends DefaultFunctionDefinition implements IGetCurrentTenantIdFunctionInvocationFactory{

    public GetCurrentTenantIdFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String returnGetCurrentTenantIdFunctionInvocation() {
        return new StringBuilder().append(getFunctionReference()).append("()").toString();
    }
}
