package com.github.starnowski.posmulten.postgresql.core;

public class EnableRowLevelSecurityProducer {

    public String produce(String table, String schema)
    {
        validateParameters(table, schema);
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE ");
        if (schema != null)
        {
            sb.append(schema);
            sb.append(".");
        }
        sb.append("\"");
        sb.append(table);
        sb.append("\"");
        sb.append(" ENABLE ROW LEVEL SECURITY;");
        return sb.toString();
    }

    private void validateParameters(String table, String schema) {
        if (schema != null && schema.trim().isEmpty())
        {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
        if (table.trim().isEmpty())
        {
            throw new IllegalArgumentException("Table name cannot be blank");
        }
    }
}
