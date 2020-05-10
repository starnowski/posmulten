package com.github.starnowski.posmulten.postgresql.core.rls.function;

public interface ISetCurrentTenantIdFunctionInvocationFactory {

    String generateStatementThatSetTenant(String tenantId);
}
