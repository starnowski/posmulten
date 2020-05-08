package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.IFunctionDefinition;

public class SetCurrentTenantIdFunctionDefinition extends DefaultFunctionDefinition implements ISetCurrentTenantIdFunctionInvocationFactory{

    public SetCurrentTenantIdFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String produce(String tenantId) {
        return null;
    }
}
