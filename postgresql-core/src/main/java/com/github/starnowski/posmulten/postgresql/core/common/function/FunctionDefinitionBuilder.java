package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.List;

public class FunctionDefinitionBuilder {

    private String createScript;

    private String functionReference;

    private String dropScript;

    private List<IFunctionArgument> functionArguments;

    public IFunctionDefinition build()
    {
        return new InnerFunctionDefinition(createScript, functionReference, dropScript, functionArguments);
    }

    public FunctionDefinitionBuilder withCreateScript(String createScript) {
        this.createScript = createScript;
        return this;
    }

    public FunctionDefinitionBuilder withFunctionReference(String functionReference) {
        this.functionReference = functionReference;
        return this;
    }

    public FunctionDefinitionBuilder withDropScript(String dropScript) {
        this.dropScript = dropScript;
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
        private final String dropScript;
        private final List<IFunctionArgument> functionArguments;

        public InnerFunctionDefinition(String createScript, String functionReference, String dropScript, List<IFunctionArgument> functionArguments) {
            this.createScript = createScript;
            this.functionReference = functionReference;
            this.dropScript = dropScript;
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

        @Override
        public String getDropScript() {
            return dropScript;
        }
    }
}
