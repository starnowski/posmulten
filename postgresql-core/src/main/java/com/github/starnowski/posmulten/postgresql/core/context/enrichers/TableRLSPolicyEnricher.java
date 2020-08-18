package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableRLSPolicySQLDefinitionsProducer;

public class TableRLSPolicyEnricher implements AbstractSharedSchemaContextEnricher {

    private TableRLSPolicySQLDefinitionsProducer tableRLSPolicySQLDefinitionsProducer = new TableRLSPolicySQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        return null;
    }

    void setTableRLSPolicySQLDefinitionsProducer(TableRLSPolicySQLDefinitionsProducer tableRLSPolicySQLDefinitionsProducer) {
        this.tableRLSPolicySQLDefinitionsProducer = tableRLSPolicySQLDefinitionsProducer;
    }
}
