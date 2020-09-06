package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;

/**
 * @since 0.2
 */
public interface IIsTenantValidFunctionInvocationFactory {

    String returnIsTenantValidFunctionInvocation(FunctionArgumentValue argumentValue);
}
