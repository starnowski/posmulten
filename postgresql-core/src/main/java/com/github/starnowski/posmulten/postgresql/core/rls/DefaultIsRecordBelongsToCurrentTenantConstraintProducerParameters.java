package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters implements IsRecordBelongsToCurrentTenantConstraintProducerParameters{

    private final String constraintName;
    private final String tableName;
    private final String tableSchema;
    private final Map<String, FunctionArgumentValue> primaryColumnsValuesMap;
    private final IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;

    public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters(String constraintName, String tableName, String tableSchema, Map<String, FunctionArgumentValue> primaryColumnsValuesMap, IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory) {
        this.constraintName = constraintName;
        this.tableName = tableName;
        this.tableSchema = tableSchema;
        this.primaryColumnsValuesMap = unmodifiableMap(primaryColumnsValuesMap);
        this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public Map<String, FunctionArgumentValue> getPrimaryColumnsValuesMap() {
        return primaryColumnsValuesMap;
    }

    public IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() {
        return isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    }
}
