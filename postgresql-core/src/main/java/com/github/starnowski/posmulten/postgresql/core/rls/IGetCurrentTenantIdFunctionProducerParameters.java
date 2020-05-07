package com.github.starnowski.posmulten.postgresql.core.rls;

public interface IGetCurrentTenantIdFunctionProducerParameters extends IFunctionFactoryParameters{

    String getFunctionName();

    String getCurrentTenantIdProperty();

    String getSchema();

    String getFunctionReturnType();
}
