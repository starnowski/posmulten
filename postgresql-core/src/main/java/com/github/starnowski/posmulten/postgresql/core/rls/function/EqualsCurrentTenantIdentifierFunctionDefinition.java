package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

public class EqualsCurrentTenantIdentifierFunctionDefinition extends DefaultFunctionDefinition implements EqualsCurrentTenantIdentifierFunctionInvocationFactory{

    public EqualsCurrentTenantIdentifierFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String returnEqualsCurrentTenantIdentifierFunctionInvocation(FunctionArgumentValue argumentValue) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFunctionReference());
        sb.append("(");
        if (FunctionArgumentValueEnum.STRING.equals(argumentValue.getType()))
        {
            sb.append("'");
        }
        sb.append(argumentValue.getValue());
        if (FunctionArgumentValueEnum.STRING.equals(argumentValue.getType()))
        {
            sb.append("'");
        }
        sb.append(")");
        return sb.toString();
    }
}
