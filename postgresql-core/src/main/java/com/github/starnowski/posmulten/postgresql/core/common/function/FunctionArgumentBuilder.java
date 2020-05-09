package com.github.starnowski.posmulten.postgresql.core.common.function;

public class FunctionArgumentBuilder {

    private String type;

    public static IFunctionArgument forType(String type)
    {
        return new FunctionArgumentBuilder().withType(type).build();
    }

    public IFunctionArgument build()
    {
        return new InnerFunctionArgument(type);
    }

    public FunctionArgumentBuilder withType(String type) {
        this.type = type;
        return this;
    }

    private static class InnerFunctionArgument implements IFunctionArgument
    {

        public InnerFunctionArgument(String type) {
            this.type = type;
        }

        private final String type;

        @Override
        public String getType() {
            return type;
        }
    }
}
