package com.github.starnowski.posmulten.postgresql.core;

public class SetNotNullStatementProducer {
    public String produce(String table, String column) {
        return "ALTER TABLE " + table + " ALTER COLUMN " + column + " SET NOT NULL;";
    }
}
