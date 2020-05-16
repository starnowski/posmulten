package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.ArrayList;
import java.util.List;

public class DefaultFunctionDefinition implements IFunctionDefinition{

    private final String createScript;
    private final String functionReference;
    private final String dropScript;
    private final List<IFunctionArgument> functionArguments;

    public DefaultFunctionDefinition(IFunctionDefinition functionDefinition)
    {
        this.createScript = functionDefinition.getCreateScript();
        this.functionReference = functionDefinition.getFunctionReference();
        this.dropScript = functionDefinition.getDropScript();
        this.functionArguments = functionDefinition.getFunctionArguments();
    }

    @Override
    public String getCreateScript() {
        return createScript;
    }

    @Override
    public String getFunctionReference() {
        return functionReference;
    }

    @Override
    public List<IFunctionArgument> getFunctionArguments() {
        return new ArrayList<>(functionArguments);
    }

    @Override
    public String getDropScript() {
        return dropScript;
    }
}
