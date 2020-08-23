package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableException;

import java.util.Set;

import static java.lang.String.format;

public class TenantColumnSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private SingleTenantColumnSQLDefinitionsProducer singleTenantColumnSQLDefinitionsProducer = new SingleTenantColumnSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) throws MissingRLSPolicyDeclarationForTableException {
        Set<TableKey> tableThatRequireCreationOfTheTenantColumn = request.getCreateTenantColumnTableLists();
        if (tableThatRequireCreationOfTheTenantColumn.isEmpty())
        {
            return context;
        }
        String getCurrentTenantIdFunctionInvocation = context.getIGetCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation();
        for (TableKey tableKey: tableThatRequireCreationOfTheTenantColumn)
        {
            AbstractTableColumns tableColumns = request.getTableColumnsList().get(tableKey);
            if (tableColumns == null)
            {
                throw new MissingRLSPolicyDeclarationForTableException(tableKey, format("Missing RLS policy declaration for table %1$s in schema %2$s", tableKey.getTable(), tableKey.getSchema()));
            }
            singleTenantColumnSQLDefinitionsProducer.produce(tableKey, tableColumns, getCurrentTenantIdFunctionInvocation, request.getCurrentTenantIdProperty(), request.getCurrentTenantIdPropertyType())
                .forEach(context::addSQLDefinition);
        }
        return context;
    }

    void setSingleTenantColumnSQLDefinitionsProducer(SingleTenantColumnSQLDefinitionsProducer singleTenantColumnSQLDefinitionsProducer) {
        this.singleTenantColumnSQLDefinitionsProducer = singleTenantColumnSQLDefinitionsProducer;
    }
}
