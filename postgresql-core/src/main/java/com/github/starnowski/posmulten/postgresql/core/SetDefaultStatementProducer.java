package com.github.starnowski.posmulten.postgresql.core;

public class SetDefaultStatementProducer {

    public String produce(ISetDefaultStatementProducerParameters parameters) {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        String table = parameters.getTable();
        String  column = parameters.getColumn();
        String defaultValueDefinition = parameters.getDefaultValueDefinition();
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
        if (defaultValueDefinition == null) {
            throw new IllegalArgumentException("Statement for default value cannot be null");
        }
        if (defaultValueDefinition.trim().isEmpty()) {
            throw new IllegalArgumentException("Statement for default value cannot be blank");
        }
        String schema = parameters.getSchema();
        String tableReference = schema == null ? table : schema + "." + table;
        return "ALTER TABLE " + tableReference + " ALTER COLUMN " + column + " SET DEFAULT " + defaultValueDefinition + ";";
    }
}
