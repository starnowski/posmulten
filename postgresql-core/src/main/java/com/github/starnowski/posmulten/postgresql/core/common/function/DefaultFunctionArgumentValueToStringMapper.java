package com.github.starnowski.posmulten.postgresql.core.common.function;

public class DefaultFunctionArgumentValueToStringMapper {

    public String map(FunctionArgumentValue value)
    {
        switch (value.getType())
        {
            case STRING:
                return "'" + value.getValue() + "'";
            case NUMERIC:
            case REFERENCE:
                return value.getValue();
        }
        return null;
    }

    public static String mapFunctionArgumentToString(FunctionArgumentValue value)
    {
        return (new DefaultFunctionArgumentValueToStringMapper()).map(value);
    }
}
