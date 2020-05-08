package com.github.starnowski.posmulten.postgresql.core;

import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            //TODO Add generic unit tests
            return this == o;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
