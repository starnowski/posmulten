package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters implements AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters{

    private final String constraintName;
    private final String tableName;
    private final String  schema;
    private final IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    private final Map<String, String> foreignKeyPrimaryKeyMappings;

    public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters(String constraintName, String tableName, String schema, IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory, Map<String, String> foreignKeyPrimaryKeyMappings) {
        this.constraintName = constraintName;
        this.tableName = tableName;
        this.schema = schema;
        this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
        this.foreignKeyPrimaryKeyMappings = foreignKeyPrimaryKeyMappings;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchema() {
        return schema;
    }

    public IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() {
        return isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    }

    public Map<String, String> getForeignKeyPrimaryKeyMappings() {
        return foreignKeyPrimaryKeyMappings;
    }

    public static IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder builder()
    {
        return new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder();
    }

    public static class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder
    {
        private String constraintName;
        private String tableName;
        private String  schema;
        private IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;
        private Map<String, String> foreignKeyPrimaryKeyMappings;

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withSchema(String schema) {
            this.schema = schema;
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
            return new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters(constraintName, tableName, schema, isRecordBelongsToCurrentTenantFunctionInvocationFactory, foreignKeyPrimaryKeyMappings);
        }
    }
}
