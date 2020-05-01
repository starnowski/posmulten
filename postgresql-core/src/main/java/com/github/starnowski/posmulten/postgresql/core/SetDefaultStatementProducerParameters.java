package com.github.starnowski.posmulten.postgresql.core;

public class SetDefaultStatementProducerParameters implements ISetDefaultStatementProducerParameters{

    private final String table;
    private final String column;
    private final String defaultValueDefinition;
    private final String schema;

    public SetDefaultStatementProducerParameters(String table, String column, String defaultValueDefinition, String schema) {
        this.table = table;
        this.column = column;
        this.defaultValueDefinition = defaultValueDefinition;
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getDefaultValueDefinition() {
        return defaultValueDefinition;
    }

    public String getSchema() {
        return schema;
    }
}
