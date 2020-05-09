package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.IFunctionFactoryParameters;

public interface IEqualsCurrentTenantIdentifierFunctionProducerParameters extends IFunctionFactoryParameters {

    String getParameterType();

    String getReturnCurrentTenantIdStatement();
}
