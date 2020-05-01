package com.github.starnowski.posmulten.postgresql.core;

public class CreateColumnStatementProducerParameters implements ICreateColumnStatementProducerParameters{

    private final String table;
    private final String column;
    private final String columnType;
    private final String schema;

    public CreateColumnStatementProducerParameters(String table, String column, String columnType, String schema) {
        this.table = table;
        this.column = column;
        this.columnType = columnType;
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getColumnType() {
        return columnType;
    }

    public String getSchema() {
        return schema;
    }
}
