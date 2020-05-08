package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.rls.IFunctionFactoryParameters;

public abstract class AbstractFunctionFactory<P extends IFunctionFactoryParameters, R extends DefaultFunctionDefinition> implements FunctionFactory<P,R> {

    @Override
    public R produce(P parameters) {
        validate(parameters);
        String createScript = produceStatement(parameters);
        String functionReference = returnFunctionReference(parameters);
        return returnFunctionDefinition(parameters, new FunctionDefinitionBuilder().withCreateScript(createScript).withFunctionReference(functionReference).build());
    }

    protected String returnFunctionReference(P parameters) {
        StringBuilder sb = new StringBuilder();
        if (parameters.getSchema() != null)
        {
            sb.append(parameters.getSchema());
            sb.append(".");
        }
        sb.append(parameters.getFunctionName());
        return sb.toString();
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

    abstract protected R returnFunctionDefinition(P parameters, IFunctionDefinition functionDefinition);

    abstract protected String produceStatement(P parameters);


}
