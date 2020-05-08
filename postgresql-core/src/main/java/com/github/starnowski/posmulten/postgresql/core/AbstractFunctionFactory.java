package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.rls.IFunctionFactoryParameters;

public abstract class AbstractFunctionFactory<P extends IFunctionFactoryParameters, R extends IFunctionDefinition> implements FunctionFactory<P,R> {

    @Override
    public R produce(P parameters) {
        validate(parameters);
        return produceStatement(parameters);
    }

    protected void validate(P parameters)
    {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        if (parameters.getFunctionName() == null)
        {
            throw new IllegalArgumentException("Function name cannot be null");
        }
        if (parameters.getFunctionName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Function name cannot be blank");
        }
        if (parameters.getSchema() != null && parameters.getSchema().trim().isEmpty())
        {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
    }

    abstract protected String produceStatement(P parameters);
}
