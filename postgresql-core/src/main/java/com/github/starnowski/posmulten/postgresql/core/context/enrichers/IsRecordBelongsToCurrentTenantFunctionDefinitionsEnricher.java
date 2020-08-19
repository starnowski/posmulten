package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantFunctionDefinitionProducer;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;

public class IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer = new IsRecordBelongsToCurrentTenantFunctionDefinitionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        return null;
    }

    void setIsRecordBelongsToCurrentTenantFunctionDefinitionProducer(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer) {
        this.isRecordBelongsToCurrentTenantFunctionDefinitionProducer = isRecordBelongsToCurrentTenantFunctionDefinitionProducer;
    }
}
