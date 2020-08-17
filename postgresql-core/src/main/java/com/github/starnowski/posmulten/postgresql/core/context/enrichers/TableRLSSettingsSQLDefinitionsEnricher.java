package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableRLSSettingsSQLDefinitionsProducer;

public class TableRLSSettingsSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private TableRLSSettingsSQLDefinitionsProducer tableRLSSettingsSQLDefinitionsProducer = new TableRLSSettingsSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        request.getTableColumnsList().keySet().forEach(tableKey ->
        {
            tableRLSSettingsSQLDefinitionsProducer.produce(tableKey, request.isForceRowLevelSecurityForTableOwner()).forEach(context::addSQLDefinition);
        });
        return context;
    }

    void setTableRLSSettingsSQLDefinitionsProducer(TableRLSSettingsSQLDefinitionsProducer tableRLSSettingsSQLDefinitionsProducer) {
        this.tableRLSSettingsSQLDefinitionsProducer = tableRLSSettingsSQLDefinitionsProducer;
    }
}
