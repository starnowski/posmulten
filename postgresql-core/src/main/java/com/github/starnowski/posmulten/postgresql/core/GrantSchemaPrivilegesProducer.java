package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.Iterator;
import java.util.List;

public class GrantSchemaPrivilegesProducer {

    public SQLDefinition produce(String schema, String user, List<String> privileges) {
        validateParameters(schema, user, privileges);
        StringBuilder sb = new StringBuilder();
        sb.append("GRANT ");
        Iterator<String> it = privileges.iterator();
        while (it.hasNext()){
            sb.append(it.next());
            if (it.hasNext())
            {
                sb.append(", ");
            }
        }
        sb.append(" ON SCHEMA ");
        sb.append(schema);
        sb.append(" TO \"");
        sb.append(user);
        sb.append("\";");
        return new DefaultSQLDefinition(prepareCreateScript(schema, user, privileges), prepareDropScript(schema, user, privileges));
    }

    private String prepareDropScript(String schema, String user, List<String> privileges) {
        StringBuilder sb = new StringBuilder();
        sb.append("REVOKE ");
        Iterator<String> it = privileges.iterator();
        while (it.hasNext()){
            sb.append(it.next());
            if (it.hasNext())
            {
                sb.append(", ");
            }
        }
        sb.append(" ON SCHEMA ");
        sb.append(schema);
        sb.append(" FROM \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private String prepareCreateScript(String schema, String user, List<String> privileges) {
        StringBuilder sb = new StringBuilder();
        sb.append("GRANT ");
        Iterator<String> it = privileges.iterator();
        while (it.hasNext()){
            sb.append(it.next());
            if (it.hasNext())
            {
                sb.append(", ");
            }
        }
        sb.append(" ON SCHEMA ");
        sb.append(schema);
        sb.append(" TO \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private void validateParameters(String schema, String user, List<String> privileges)
    {
        if (schema == null)
        {
            throw new IllegalArgumentException("Schema name cannot be null");
        }
        if (schema.trim().isEmpty())
        {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
        if (user == null)
        {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (user.trim().isEmpty())
        {
            throw new IllegalArgumentException("user cannot be blank");
        }
        if (privileges == null)
        {
            throw new IllegalArgumentException("privileges list cannot be null");
        }
        if (privileges.isEmpty())
        {
            throw new IllegalArgumentException("privileges list cannot be empty");
        }
    }
}
