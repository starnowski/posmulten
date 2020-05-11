package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionFactoryParameters;

public interface ITenantHasAuthoritiesFunctionProducerParameters extends IFunctionFactoryParameters {

    String getTenantIdArgumentType();

    String getUsingExpressionArgumentType();

    String getCheckWithExpressionArgumentType();

    String getTableArgumentType();

    String getSchemaArgumentType();

    EqualsCurrentTenantIdentifierFunctionInvocationFactory getEqualsCurrentTenantIdentifierFunctionInvocationFactory();
}
