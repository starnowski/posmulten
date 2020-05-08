package com.github.starnowski.posmulten.postgresql.core;

import java.util.List;

public class FunctionDefinitionBuilder {

    private String createScript;

    private String functionReference;

    private List<IFunctionArgument> functionArguments;

    public IFunctionDefinition build()
    {
        return new InnerFunctionDefinition(createScript, functionReference, functionArguments);
    }

    public FunctionDefinitionBuilder withCreateScript(String createScript) {
        this.createScript = createScript;
        return this;
    }

    public FunctionDefinitionBuilder withFunctionReference(String functionReference) {
        this.functionReference = functionReference;
        return this;
    }

    public FunctionDefinitionBuilder withFunctionArguments(List<IFunctionArgument> functionArguments) {
        this.functionArguments = functionArguments;
        return this;
    }

    private static class InnerFunctionDefinition implements IFunctionDefinition
    {
        private final String createScript;
        private final String functionReference;
        private final List<IFunctionArgument> functionArguments;

        public InnerFunctionDefinition(String createScript, String functionReference, List<IFunctionArgument> functionArguments) {
            this.createScript = createScript;
            this.functionReference = functionReference;
            this.functionArguments = functionArguments;
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
            return functionArguments;
        }
    }
}
