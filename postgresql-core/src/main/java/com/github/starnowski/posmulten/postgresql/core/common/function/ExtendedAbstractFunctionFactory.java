package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.List;

public abstract class ExtendedAbstractFunctionFactory<P extends IFunctionFactoryParameters, R extends DefaultFunctionDefinition> extends AbstractFunctionFactory<P, R>{

    @Override
    protected String produceStatement(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildFunctionNameAndArgumentDeclaration(parameters));
        sb.append(" as $$");
        sb.append("\n");
        sb.append(buildBodyAndMetaData(parameters));
        sb.append(";");
        return sb.toString();
    }

    protected String buildFunctionNameAndArgumentDeclaration(P parameters)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE OR REPLACE FUNCTION ");
        sb.append(returnFunctionReference(parameters));
        sb.append(buildArgumentDeclaration(parameters));
        sb.append(" ");
        sb.append(buildReturnPhrase(parameters));
        return sb.toString();
    }

    private String buildReturnPhrase(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("RETURNS ");
        sb.append(prepareReturnType(parameters));
        return sb.toString();
    }

    protected abstract String prepareReturnType(P parameters);

    protected String buildArgumentDeclaration(P parameters)
    {
        List<IFunctionArgument> arguments = prepareFunctionArguments(parameters);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(prepareArgumentsPhrase(arguments));
        sb.append(")");
        return sb.toString();
    }

    protected String buildBodyAndMetaData(P parameters)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(buildBody(parameters));
        sb.append("\n");
        sb.append("$$ LANGUAGE ");
        sb.append(returnFunctionLanguage(parameters));
        sb.append("\n");
        sb.append(buildMetaData(parameters));
        return sb.toString();
    }

    protected abstract String buildMetaData(P parameters);

    protected abstract String buildBody(P parameters);

    protected String returnFunctionLanguage(P parameters)
    {
        return "sql";
    }
}
