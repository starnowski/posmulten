package com.github.starnowski.posmulten.postgresql.core.rls.function;

public interface ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory {

    String returnPreparedStatementThatSetCurrentTenant();
}
