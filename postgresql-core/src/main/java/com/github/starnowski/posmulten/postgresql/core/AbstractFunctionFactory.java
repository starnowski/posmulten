package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.rls.IFunctionFactoryParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public abstract class AbstractFunctionFactory<P extends IFunctionFactoryParameters, R extends DefaultFunctionDefinition> implements FunctionFactory<P,R> {

    @Override
    public R produce(P parameters) {
        validate(parameters);
        String createScript = produceStatement(parameters);
        String functionReference = returnFunctionReference(parameters);
        String dropScript = returnDropScript(parameters);
        return returnFunctionDefinition(parameters, new FunctionDefinitionBuilder().withCreateScript(createScript).withFunctionReference(functionReference).withDropScript(dropScript).build());
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

    protected String returnDropScript(P parameters) {
        List<IFunctionArgument> arguments = prepareFunctionArguments(parameters);
        return format("DROP FUNCTION IF EXISTS %s(%s)", returnFunctionReference(parameters), prepareArgumentsPhrase(arguments));
    }

    protected String prepareArgumentsPhrase(List<IFunctionArgument> functionArguments)
    {
        return ofNullable(functionArguments).orElse(new ArrayList<IFunctionArgument>()).stream().map(IFunctionArgument::getType).collect(Collectors.joining( ", " ));
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

    abstract protected List<IFunctionArgument> prepareFunctionArguments(P parameters);
}
