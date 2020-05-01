package com.github.starnowski.posmulten.postgresql.core;

public class SetNotNullStatementProducer {
    public String produce(ISetNotNullStatementProducerParameters parameters) {
        String table = parameters.getTable();
        String column = parameters.getColumn();
        String schema = parameters.getSchema();
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (table.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be blank");
        }
        if (column == null) {
            throw new IllegalArgumentException("Column name cannot be null");
        }
        if (column.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be blank");
        }
        return "ALTER TABLE " + table + " ALTER COLUMN " + column + " SET NOT NULL;";
    }
}
