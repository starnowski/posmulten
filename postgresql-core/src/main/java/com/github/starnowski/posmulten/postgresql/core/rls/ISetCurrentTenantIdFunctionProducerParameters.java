package com.github.starnowski.posmulten.postgresql.core.rls;

public interface ISetCurrentTenantIdFunctionProducerParameters extends IFunctionFactoryParameters{

    String getArgumentType();

    String getCurrentTenantIdProperty();
}
