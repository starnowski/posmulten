package com.github.starnowski.posmulten.postgresql.core;

public class FunctionDefinitionBuilder {

    private String createScript;

    public IFunctionDefinition build()
    {
        return new InnerFunctionDefinition(createScript);
    }

    public String getCreateScript() {
        return createScript;
    }

    public FunctionDefinitionBuilder withCreateScript(String createScript) {
        this.createScript = createScript;
        return this;
    }

    private static class InnerFunctionDefinition implements IFunctionDefinition
    {
        private final String createScript;

        public InnerFunctionDefinition(String createScript) {
            this.createScript = createScript;
        }

        @Override
        public String getCreateScript() {
            return createScript;
        }
    }
}
