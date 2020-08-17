package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableRLSSettingsSQLDefinitionsProducer;

public class TableRLSSettingsSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private TableRLSSettingsSQLDefinitionsProducer tableRLSSettingsSQLDefinitionsProducer = new TableRLSSettingsSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        return null;
    }

    void setTableRLSSettingsSQLDefinitionsProducer(TableRLSSettingsSQLDefinitionsProducer tableRLSSettingsSQLDefinitionsProducer) {
        this.tableRLSSettingsSQLDefinitionsProducer = tableRLSSettingsSQLDefinitionsProducer;
    }
}
