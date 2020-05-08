package com.github.starnowski.posmulten.postgresql.core.rls;

public interface ISetCurrentTenantIdFunctionInvocationFactory {

    String generateStatementThatSetTenant(String tenantId);
}
