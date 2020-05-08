package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.rls.IFunctionFactoryParameters;

public interface FunctionFactory<P extends IFunctionFactoryParameters, R extends IFunctionDefinition> {

    R produce(P parameters);
}
