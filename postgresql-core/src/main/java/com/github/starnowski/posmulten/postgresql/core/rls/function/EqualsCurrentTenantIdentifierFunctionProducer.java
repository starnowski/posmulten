package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

public class EqualsCurrentTenantIdentifierFunctionProducer extends AbstractFunctionFactory<IEqualsCurrentTenantIdentifierFunctionProducerParameters, DefaultFunctionDefinition> {
    @Override
    protected DefaultFunctionDefinition returnFunctionDefinition(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new DefaultFunctionDefinition(functionDefinition);
    }

    @Override
    protected String produceStatement(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE OR REPLACE FUNCTION ");
        if (parameters.getSchema() != null)
        {
            sb.append(parameters.getSchema());
            sb.append(".");
        }
        sb.append(parameters.getFunctionName());
        sb.append("(");
        if (parameters.getArgumentType() == null)
        {
            sb.append("VARCHAR(255)");
        }
        else
        {
            sb.append(parameters.getArgumentType());
        }
        sb.append(")");
        sb.append(" RETURNS BOOLEAN");
        sb.append(" as $$");
        sb.append("\n");
        sb.append("SELECT $1 = ");
        sb.append(parameters.getCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation());
        sb.append("\n");
        sb.append("$$ LANGUAGE sql");
        sb.append("\n");
        sb.append("STABLE PARALLEL SAFE");
        sb.append(";");
        return sb.toString();
    }

}
