package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

public class ForceRowLevelSecurityProducer {

    public SQLDefinition produce(String table, String schema)
    {
        validateParameters(table, schema);
        return new DefaultSQLDefinition(prepareCreateScript(table, schema), prepareDropScript(table, schema));
    }

    private String prepareDropScript(String table, String schema) {
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
        sb.append(" NO FORCE ROW LEVEL SECURITY;");
        return sb.toString();
    }

    private String prepareCreateScript(String table, String schema) {
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
        sb.append(" FORCE ROW LEVEL SECURITY;");
        return sb.toString();
    }

    private void validateParameters(String table, String schema) {
        if (schema != null && schema.trim().isEmpty())
        {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
        if (table == null)
        {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (table.trim().isEmpty())
        {
            throw new IllegalArgumentException("Table name cannot be blank");
        }
    }
}
