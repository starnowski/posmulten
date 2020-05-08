package com.github.starnowski.posmulten.postgresql.core;

public class DefaultFunctionDefinition implements IFunctionDefinition{

    private final String createScript;
    private final String functionReference;

    public DefaultFunctionDefinition(IFunctionDefinition functionDefinition)
    {
        this.createScript = functionDefinition.getCreateScript();
        this.functionReference = functionDefinition.getFunctionReference();
    }

    @Override
    public String getCreateScript() {
        return createScript;
    }

    @Override
    public String getFunctionReference() {
        return functionReference;
    }
}
