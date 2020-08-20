package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.Objects;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum.*;

public interface FunctionArgumentValue {

    String getValue();

    FunctionArgumentValueEnum getType();

    static FunctionArgumentValue forString(String value)
    {
        return new DefaultFunctionArgumentValue(value, STRING);
    }

    static FunctionArgumentValue forReference(String value)
    {
        return new DefaultFunctionArgumentValue(value, REFERENCE);
    }

    static FunctionArgumentValue forNumeric(String value)
    {
        return new DefaultFunctionArgumentValue(value, NUMERIC);
    }

    final class DefaultFunctionArgumentValue implements FunctionArgumentValue {

        private final String value;

        private final FunctionArgumentValueEnum type;
        public DefaultFunctionArgumentValue(String value, FunctionArgumentValueEnum type) {
            this.value = value;
            this.type = type;
        }


        @Override
        public String getValue() {
            return value;
        }

        @Override
        public FunctionArgumentValueEnum getType() {
            return type;
        }

        @Override
        public String toString() {
            return "DefaultFunctionArgumentValue{" +
                    "value='" + value + '\'' +
                    ", type=" + type +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DefaultFunctionArgumentValue that = (DefaultFunctionArgumentValue) o;
            return Objects.equals(value, that.value) &&
                    type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, type);
        }

    }
}
