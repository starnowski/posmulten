package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.CreateColumnStatementProducer;
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer;
import com.github.starnowski.posmulten.postgresql.core.SetNotNullStatementProducer;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.List;

public class SingleTenantColumnSQLDefinitionsProducer {

    private CreateColumnStatementProducer createColumnStatementProducer = new CreateColumnStatementProducer();
    private SetDefaultStatementProducer setDefaultStatementProducer = new SetDefaultStatementProducer();
    private SetNotNullStatementProducer setNotNullStatementProducer = new SetNotNullStatementProducer();

    public List<SQLDefinition> produce(TableKey tableKey, AbstractTableColumns tableColumns, String defaultTenantColumnValue, String defaultTenantColumn, String defaultTenantColumnType)
    {
        //TODO
        return null;
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
