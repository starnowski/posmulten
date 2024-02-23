package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class DefaultForeignKeyConstraintStatementParameters implements IForeignKeyConstraintStatementParameters {

    private final Map<String, String> foreignKeyColumnMappings;
    private final String constraintName;
    private final String tableName;
    private final String tableSchema;
    private final TableKey referenceTableKey;

    public DefaultForeignKeyConstraintStatementParameters(Map<String, String> foreignKeyColumnMappings, String constraintName, String tableName, String tableSchema, TableKey referenceTableKey) {
        this.foreignKeyColumnMappings = foreignKeyColumnMappings;
        this.constraintName = constraintName;
        this.tableName = tableName;
        this.tableSchema = tableSchema;
        this.referenceTableKey = referenceTableKey;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Map<String, String> getForeignKeyColumnMappings() {
        return foreignKeyColumnMappings;
    }

    @Override
    public TableKey getReferenceTableKey() {
        return referenceTableKey;
    }

    @Override
    public String getConstraintName() {
        return constraintName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getTableSchema() {
        return tableSchema;
    }

    public static class Builder {
        private Map<String, String> foreignKeyColumnMappings;
        private String constraintName;
        private String tableName;
        private String tableSchema;
        private TableKey referenceTableKey;

        public Builder withForeignKeyColumnMappings(Map<String, String> foreignKeyColumnMappings) {
            this.foreignKeyColumnMappings = foreignKeyColumnMappings;
            return this;
        }

        public Builder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder withTableSchema(String tableSchema) {
            this.tableSchema = tableSchema;
            return this;
        }

        public Builder withReferenceTableKey(TableKey referenceTableKey) {
            this.referenceTableKey = referenceTableKey;
            return this;
        }

        public DefaultForeignKeyConstraintStatementParameters build() {
            return new DefaultForeignKeyConstraintStatementParameters(unmodifiableMap(this.foreignKeyColumnMappings), this.constraintName, this.tableName, this.tableSchema, this.referenceTableKey);
        }

    }
}
