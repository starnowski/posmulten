package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.Objects;

public final class TableKey {

    private final String table;
    private final String schema;

    public TableKey(String table, String schema) {
        this.table = table;
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public String getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "TableKey{" +
                "table='" + table + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableKey tableKey = (TableKey) o;
        return Objects.equals(table, tableKey.table) &&
                Objects.equals(schema, tableKey.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, schema);
    }

}
