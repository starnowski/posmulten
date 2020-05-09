package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.IFunctionFactoryParameters;

public interface ISetCurrentTenantIdFunctionProducerParameters extends IFunctionFactoryParameters {

    String getArgumentType();

    String getCurrentTenantIdProperty();
}
