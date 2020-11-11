package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

import static com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionArgumentValueToStringMapper.mapFunctionArgumentToString;

/**
 * @since 0.2
 */
public class IsTenantValidBasedOnConstantValuesFunctionDefinition extends DefaultFunctionDefinition implements IIsTenantValidFunctionInvocationFactory{

    public IsTenantValidBasedOnConstantValuesFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String returnIsTenantValidFunctionInvocation(FunctionArgumentValue argumentValue) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getFunctionReference());
        sb.append("(");
        sb.append(mapFunctionArgumentToString(argumentValue));
        sb.append(")");
        return sb.toString();
    }
}
