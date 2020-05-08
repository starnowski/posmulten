package com.github.starnowski.posmulten.postgresql.core;

public class FunctionDefinitionBuilder {

    private String createScript;

    private String functionReference;

    public IFunctionDefinition build()
    {
        return new InnerFunctionDefinition(createScript, functionReference);
    }

    public String getCreateScript() {
        return createScript;
    }

    public FunctionDefinitionBuilder withCreateScript(String createScript) {
        this.createScript = createScript;
        return this;
    }

    public String getFunctionReference() {
        return functionReference;
    }

    public FunctionDefinitionBuilder withFunctionReference(String functionReference) {
        this.functionReference = functionReference;
        return this;
    }

    private static class InnerFunctionDefinition implements IFunctionDefinition
    {
        private final String createScript;
        private final String functionReference;

        public InnerFunctionDefinition(String createScript, String functionReference) {
            this.createScript = createScript;
            this.functionReference = functionReference;
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
}
