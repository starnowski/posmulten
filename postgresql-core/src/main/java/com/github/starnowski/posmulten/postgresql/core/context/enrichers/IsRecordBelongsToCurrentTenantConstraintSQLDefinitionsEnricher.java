package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        return null;
    }

    void setIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer) {
        this.isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer;
    }
}
