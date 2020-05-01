package com.github.starnowski.posmulten.postgresql.core;

public class SetNotNullStatementProducer {
    public String produce(ISetNotNullStatementProducerParameters parameters) {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
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
        String tableReference = schema == null ? table : schema + "." + table;
        return "ALTER TABLE " + tableReference + " ALTER COLUMN " + column + " SET NOT NULL;";
    }
}
