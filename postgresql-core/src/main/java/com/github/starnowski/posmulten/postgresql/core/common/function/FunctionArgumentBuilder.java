package com.github.starnowski.posmulten.postgresql.core.common.function;

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
        public String toString() {
            return "InnerFunctionArgument{" +
                    "type='" + type + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerFunctionArgument that = (InnerFunctionArgument) o;
            return Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
