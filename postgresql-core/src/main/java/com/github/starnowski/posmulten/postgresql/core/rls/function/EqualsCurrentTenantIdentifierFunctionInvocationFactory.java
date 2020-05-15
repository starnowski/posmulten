package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;

public interface EqualsCurrentTenantIdentifierFunctionInvocationFactory {

    String returnEqualsCurrentTenantIdentifierFunctionInvocation(FunctionArgumentValue argumentValue);
}
