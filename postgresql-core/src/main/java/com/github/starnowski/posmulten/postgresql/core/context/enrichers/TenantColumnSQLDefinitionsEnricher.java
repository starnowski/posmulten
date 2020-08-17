package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;

public class TenantColumnSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private SingleTenantColumnSQLDefinitionsProducer singleTenantColumnSQLDefinitionsProducer = new SingleTenantColumnSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        //TODO Add exception for non-existed table declaration for which rls policy should be created
        String getCurrentTenantIdFunctionInvocation = context.getIGetCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation();
        for (TableKey tableKey: request.getCreateTenantColumnTableLists())
        {
            AbstractTableColumns tableColumns = request.getTableColumnsList().get(tableKey);
            singleTenantColumnSQLDefinitionsProducer.produce(tableKey, tableColumns, getCurrentTenantIdFunctionInvocation, request.getCurrentTenantIdProperty(), request.getCurrentTenantIdPropertyType())
                .forEach(context::addSQLDefinition);
        }
        return context;
    }

    void setSingleTenantColumnSQLDefinitionsProducer(SingleTenantColumnSQLDefinitionsProducer singleTenantColumnSQLDefinitionsProducer) {
        this.singleTenantColumnSQLDefinitionsProducer = singleTenantColumnSQLDefinitionsProducer;
    }
}
