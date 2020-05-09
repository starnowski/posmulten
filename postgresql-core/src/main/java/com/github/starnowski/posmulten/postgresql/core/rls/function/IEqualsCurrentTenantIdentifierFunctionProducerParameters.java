package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionFactoryParameters;

public interface IEqualsCurrentTenantIdentifierFunctionProducerParameters extends IFunctionFactoryParameters {

    String getParameterType();

    String getReturnCurrentTenantIdStatement();

    IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory();
}
