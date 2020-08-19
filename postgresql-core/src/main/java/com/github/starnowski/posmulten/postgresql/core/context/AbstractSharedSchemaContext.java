package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.List;
import java.util.Map;

public interface AbstractSharedSchemaContext {

    List<SQLDefinition> getSqlDefinitions();

    void addSQLDefinition(SQLDefinition sqlDefinition);

    TenantHasAuthoritiesFunctionInvocationFactory getTenantHasAuthoritiesFunctionInvocationFactory();

    void setTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory factory);

    IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory();

    void setIGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory factory);

    void setISetCurrentTenantIdFunctionInvocationFactory(ISetCurrentTenantIdFunctionInvocationFactory factory);

    ISetCurrentTenantIdFunctionInvocationFactory getISetCurrentTenantIdFunctionInvocationFactory();

    Map<TableKey, IsRecordBelongsToCurrentTenantFunctionInvocationFactory> getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap();
}
