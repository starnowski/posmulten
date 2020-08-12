package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;

import java.util.List;

public class SharedSchemaContext implements AbstractSharedSchemaContext {

    private IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;

    @Override
    public List<SQLDefinition> getSqlDefinitions() {
        return null;
    }

    @Override
    public void addSQLDefinition(SQLDefinition sqlDefinition) {

    }

    @Override
    public EqualsCurrentTenantIdentifierFunctionInvocationFactory getEqualsCurrentTenantIdentifierFunctionInvocationFactory() {
        return null;
    }

    @Override
    public IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory() {
        return iGetCurrentTenantIdFunctionInvocationFactory;
    }

    @Override
    public void setIGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory factory) {
        this.iGetCurrentTenantIdFunctionInvocationFactory = factory;
    }

    @Override
    public ISetCurrentTenantIdFunctionInvocationFactory getISetCurrentTenantIdFunctionInvocationFactory() {
        return null;
    }
}
