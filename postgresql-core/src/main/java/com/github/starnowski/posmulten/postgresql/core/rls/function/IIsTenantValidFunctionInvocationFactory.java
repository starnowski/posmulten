package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;

public interface IIsTenantValidFunctionInvocationFactory {

    String returnIsTenantValidFunctionInvocation(FunctionArgumentValue argumentValue);
}
