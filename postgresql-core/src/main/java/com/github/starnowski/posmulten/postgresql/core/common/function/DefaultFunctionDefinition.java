package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        this.functionArguments = new ArrayList<>(Optional.ofNullable(functionDefinition.getFunctionArguments()).orElseThrow(() -> new IllegalArgumentException("Function argument collection cannot be null")));
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
