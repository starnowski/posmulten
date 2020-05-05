package com.github.starnowski.posmulten.postgresql.core.rls;

public interface IGetCurrentTenantIdFunctionProducerParameters {

    String getFunctionName();

    String getCurrentTenantIdProperty();

    String getSchema();

    String getFunctionReturnType();
}
