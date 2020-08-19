package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantProducer;

public class IsRecordBelongsToCurrentTenantFunctionDefinitionProducer {

    private IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();

    public IsRecordBelongsToCurrentTenantFunctionDefinition produce(TableKey tableKey, AbstractTableColumns tableColumns, IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory, String functionName)
    {
        //TODO
        return null;
    }

    void setIsRecordBelongsToCurrentTenantProducer(IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer) {
        this.isRecordBelongsToCurrentTenantProducer = isRecordBelongsToCurrentTenantProducer;
    }
}
