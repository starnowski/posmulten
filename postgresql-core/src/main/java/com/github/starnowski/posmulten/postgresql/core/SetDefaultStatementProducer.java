package com.github.starnowski.posmulten.postgresql.core;

public class SetDefaultStatementProducer {

    public String produce(String table, String column, String defaultValueDefinition) {
        return "ALTER TABLE " + table + " ALTER COLUMN " + column + " SET DEFAULT " + defaultValueDefinition + ";";
    }
}
