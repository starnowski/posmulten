package com.github.starnowski.posmulten.postgresql.core.common.function;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum.STRING;

public class FunctionArgumentValueToStringMapper {

    public String map(FunctionArgumentValue value)
    {
        return value == null ? null : (STRING.equals(value.getType()) ? ("'" + value.getValue() + "'") : value.getValue());
    }

    public static String mapToString(FunctionArgumentValue value)
    {
        return (new FunctionArgumentValueToStringMapper()).map(value);
    }
}
