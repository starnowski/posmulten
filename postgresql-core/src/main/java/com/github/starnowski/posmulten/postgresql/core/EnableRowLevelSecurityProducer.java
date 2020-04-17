package com.github.starnowski.posmulten.postgresql.core;

public class EnableRowLevelSecurityProducer {

    public String produce(String table, String schema)
    {
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
}
