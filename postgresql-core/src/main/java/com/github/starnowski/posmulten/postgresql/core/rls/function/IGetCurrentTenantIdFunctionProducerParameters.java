package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionFactoryParameters;

public interface IGetCurrentTenantIdFunctionProducerParameters extends IFunctionFactoryParameters {

    String getFunctionName();

    String getCurrentTenantIdProperty();

    String getSchema();

    String getFunctionReturnType();
}
