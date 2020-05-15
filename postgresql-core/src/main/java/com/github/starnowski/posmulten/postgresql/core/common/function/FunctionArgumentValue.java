package com.github.starnowski.posmulten.postgresql.core.common.function;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum.REFERENCE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum.STRING;

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

    class DefaultFunctionArgumentValue implements FunctionArgumentValue {

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

    }
}
