package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public final class DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters implements IsRecordBelongsToCurrentTenantConstraintProducerParameters{

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

    public static DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder builder()
    {
        return new DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder();
    }

    public static class DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder
    {
        private String constraintName;
        private String tableName;
        private String tableSchema;
        private Map<String, FunctionArgumentValue> primaryColumnsValuesMap;
        private IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withTableSchema(String tableSchema) {
            this.tableSchema = tableSchema;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withPrimaryColumnsValuesMap(Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
            this.primaryColumnsValuesMap = primaryColumnsValuesMap;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory) {
            this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters build()
        {
            return new DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters(constraintName, tableName, tableSchema, primaryColumnsValuesMap, isRecordBelongsToCurrentTenantFunctionInvocationFactory);
        }
    }
}
