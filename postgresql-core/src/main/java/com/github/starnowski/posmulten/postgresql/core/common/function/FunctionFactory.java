package com.github.starnowski.posmulten.postgresql.core.common.function;

public interface FunctionFactory<P extends IFunctionFactoryParameters, R extends IFunctionDefinition> {

    R produce(P parameters);
}
