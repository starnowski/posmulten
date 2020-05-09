package com.github.starnowski.posmulten.postgresql.core;

public interface FunctionFactory<P extends IFunctionFactoryParameters, R extends IFunctionDefinition> {

    R produce(P parameters);
}
