package com.github.starnowski.posmulten.postgresql.core;

public class SetNotNullStatementProducerParameters implements ISetNotNullStatementProducerParameters{
    private final String table;
    private final String column;
    private final String schema;

    public SetNotNullStatementProducerParameters(String table, String column, String schema) {
        this.table = table;
        this.column = column;
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getSchema() {
        return schema;
    }
}
