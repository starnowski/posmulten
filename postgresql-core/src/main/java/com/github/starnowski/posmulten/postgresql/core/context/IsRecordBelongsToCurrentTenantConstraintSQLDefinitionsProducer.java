package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer {

    private IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer = new IsRecordBelongsToCurrentTenantConstraintProducer();

    public List<SQLDefinition> produce(AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters parameters)
    {
        IsRecordBelongsToCurrentTenantConstraintProducerParameters isRecordBelongsToCurrentTenantConstraintProducerParameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(parameters.getConstraintName())
                .withTableName(parameters.getTableKey().getTable())
                .withTableSchema(parameters.getTableKey().getSchema())
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory())
                .withPrimaryColumnsValuesMap(convertForeignPrimaryKeyMapping(parameters.getForeignKeyPrimaryKeyMappings()))
                .build();
        return Collections.singletonList(isRecordBelongsToCurrentTenantConstraintProducer.produce(isRecordBelongsToCurrentTenantConstraintProducerParameters));
    }

    void setIsRecordBelongsToCurrentTenantConstraintProducer(IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer) {
        this.isRecordBelongsToCurrentTenantConstraintProducer = isRecordBelongsToCurrentTenantConstraintProducer;
    }

    private Map<String, FunctionArgumentValue> convertForeignPrimaryKeyMapping(Map<String, String> foreignKeyPrimaryKeyMappings)
    {
        return foreignKeyPrimaryKeyMappings.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry-> forReference(entry.getValue())));
    }
}
