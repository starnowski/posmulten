package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.*;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.ArrayList;
import java.util.List;

public class SingleTenantColumnSQLDefinitionsProducer {

    private CreateColumnStatementProducer createColumnStatementProducer = new CreateColumnStatementProducer();
    private SetDefaultStatementProducer setDefaultStatementProducer = new SetDefaultStatementProducer();
    private SetNotNullStatementProducer setNotNullStatementProducer = new SetNotNullStatementProducer();

    public List<SQLDefinition> produce(TableKey tableKey, AbstractTableColumns tableColumns, String defaultTenantColumnValue, String defaultTenantColumn, String defaultTenantColumnType)
    {
        List<SQLDefinition> results = new ArrayList<>();
        String tenantColumn = tableColumns.getTenantColumnName() == null ? defaultTenantColumn : tableColumns.getTenantColumnName();
        results.add(createColumnStatementProducer.produce(new CreateColumnStatementProducerParameters(tableKey.getTable(), tenantColumn, defaultTenantColumnType, tableKey.getSchema())));
        results.add(setDefaultStatementProducer.produce(new SetDefaultStatementProducerParameters(tableKey.getTable(), tenantColumn, defaultTenantColumnValue, tableKey.getSchema())));
        results.add(setNotNullStatementProducer.produce(new SetNotNullStatementProducerParameters(tableKey.getTable(), tenantColumn, tableKey.getSchema())));
        return results;
    }

    public void setCreateColumnStatementProducer(CreateColumnStatementProducer createColumnStatementProducer) {
        this.createColumnStatementProducer = createColumnStatementProducer;
    }

    public void setSetDefaultStatementProducer(SetDefaultStatementProducer setDefaultStatementProducer) {
        this.setDefaultStatementProducer = setDefaultStatementProducer;
    }

    public void setSetNotNullStatementProducer(SetNotNullStatementProducer setNotNullStatementProducer) {
        this.setNotNullStatementProducer = setNotNullStatementProducer;
    }

}
