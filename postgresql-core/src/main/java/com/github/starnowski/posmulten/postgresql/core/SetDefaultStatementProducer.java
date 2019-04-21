package com.github.starnowski.posmulten.postgresql.core;

public class SetDefaultStatementProducer {

    public String produce(String table, String column, String defaultValueDefinition) {
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
        return "ALTER TABLE " + table + " ALTER COLUMN " + column + " SET DEFAULT " + defaultValueDefinition + ";";
    }
}
