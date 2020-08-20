package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer;

import java.util.List;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer {

    private IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer = new IsRecordBelongsToCurrentTenantConstraintProducer();

    public List<SQLDefinition> produce(AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters parameters)
    {
        return null;
    }

    void setIsRecordBelongsToCurrentTenantConstraintProducer(IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer) {
        this.isRecordBelongsToCurrentTenantConstraintProducer = isRecordBelongsToCurrentTenantConstraintProducer;
    }
}
