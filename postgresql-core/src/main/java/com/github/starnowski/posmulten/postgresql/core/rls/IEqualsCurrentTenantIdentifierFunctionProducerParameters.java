package com.github.starnowski.posmulten.postgresql.core.rls;

public interface IEqualsCurrentTenantIdentifierFunctionProducerParameters extends IFunctionFactoryParameters{

    String getParameterType();

    String getReturnCurrentTenantIdStatement();
}
