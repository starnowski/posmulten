package com.github.starnowski.posmulten.postgresql.core;

public class DefaultFunctionDefinition implements IFunctionDefinition{

    private String createScript;

    public DefaultFunctionDefinition(IFunctionDefinition functionDefinition)
    {
        this.createScript = functionDefinition.getCreateScript();
    }

    @Override
    public String getCreateScript() {
        return createScript;
    }
}
