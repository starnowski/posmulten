package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.IFunctionDefinition;

public class SetCurrentTenantIdFunctionDefinition extends DefaultFunctionDefinition implements ISetCurrentTenantIdFunctionInvocationFactory {

    public SetCurrentTenantIdFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String generateStatementThatSetTenant(String tenantId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(getFunctionReference());
        sb.append("('");
        sb.append(tenantId);
        sb.append("');");
        return sb.toString();
    }
}
