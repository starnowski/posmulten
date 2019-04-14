package com.github.starnowski.posmulten.postgresql.core;

public class SetNotNullStatementProducer {
    public String produce(String table, String column) {
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        return "ALTER TABLE " + table + " ALTER COLUMN " + column + " SET NOT NULL;";
    }
}
