package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.rls.IFunctionFactoryParameters;

public interface FunctionFactory<P extends IFunctionFactoryParameters> {

    String produce(P parameters);
}
