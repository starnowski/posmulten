package com.github.starnowski.posmulten.postgresql.core.rls;

public interface FunctionFactory<P extends IFunctionFactoryParameters> {

    String produce(P parameters);
}
