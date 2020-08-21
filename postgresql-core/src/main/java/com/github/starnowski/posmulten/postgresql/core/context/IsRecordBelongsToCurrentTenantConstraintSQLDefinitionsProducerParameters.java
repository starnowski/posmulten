package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;
import java.util.Objects;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters implements AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters{

    private final String constraintName;
    private final TableKey tableKey;
    private final IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    private final Map<String, String> foreignKeyPrimaryKeyMappings;

    public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters(String constraintName, TableKey tableKey, IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory, Map<String, String> foreignKeyPrimaryKeyMappings) {
        this.constraintName = constraintName;
        this.tableKey = tableKey;
        this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
        this.foreignKeyPrimaryKeyMappings = foreignKeyPrimaryKeyMappings;
    }

    @Override
    public TableKey getTableKey() {
        return tableKey;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() {
        return isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    }

    public Map<String, String> getForeignKeyPrimaryKeyMappings() {
        return foreignKeyPrimaryKeyMappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters that = (IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters) o;
        return Objects.equals(constraintName, that.constraintName) &&
                Objects.equals(tableKey, that.tableKey) &&
                Objects.equals(isRecordBelongsToCurrentTenantFunctionInvocationFactory, that.isRecordBelongsToCurrentTenantFunctionInvocationFactory) &&
                Objects.equals(foreignKeyPrimaryKeyMappings, that.foreignKeyPrimaryKeyMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraintName, tableKey, isRecordBelongsToCurrentTenantFunctionInvocationFactory, foreignKeyPrimaryKeyMappings);
    }

    public static IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder builder()
    {
        return new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder();
    }

    public static class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder
    {
        private String constraintName;
        private TableKey tableKey;
        private IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;
        private Map<String, String> foreignKeyPrimaryKeyMappings;

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withTableKey(TableKey tableKey) {
            this.tableKey = tableKey;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory) {
            this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withForeignKeyPrimaryKeyMappings(Map<String, String> foreignKeyPrimaryKeyMappings) {
            this.foreignKeyPrimaryKeyMappings = foreignKeyPrimaryKeyMappings;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters build()
        {
            return new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters(constraintName, tableKey, isRecordBelongsToCurrentTenantFunctionInvocationFactory, foreignKeyPrimaryKeyMappings);
        }
    }
}
