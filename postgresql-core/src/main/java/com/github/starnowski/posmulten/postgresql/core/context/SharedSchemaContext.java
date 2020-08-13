package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;

import java.util.ArrayList;
import java.util.List;

public class SharedSchemaContext implements AbstractSharedSchemaContext {

    private IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;
    private ISetCurrentTenantIdFunctionInvocationFactory iSetCurrentTenantIdFunctionInvocationFactory;
    private List<SQLDefinition> sqlDefinitions = new ArrayList<>();

    @Override
    public List<SQLDefinition> getSqlDefinitions() {
        return new ArrayList<>(sqlDefinitions);
    }

    @Override
    public void addSQLDefinition(SQLDefinition sqlDefinition) {
        sqlDefinitions.add(sqlDefinition);
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
    public void setISetCurrentTenantIdFunctionInvocationFactory(ISetCurrentTenantIdFunctionInvocationFactory factory) {
        this.iSetCurrentTenantIdFunctionInvocationFactory = factory;
    }

    @Override
    public ISetCurrentTenantIdFunctionInvocationFactory getISetCurrentTenantIdFunctionInvocationFactory() {
        return iSetCurrentTenantIdFunctionInvocationFactory;
    }
}
