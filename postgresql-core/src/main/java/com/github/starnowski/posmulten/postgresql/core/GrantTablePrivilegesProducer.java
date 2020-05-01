package com.github.starnowski.posmulten.postgresql.core;

import java.util.Iterator;
import java.util.List;

/**
 * Service creates a SQL statement that grants privileges to database table.
 */
public class GrantTablePrivilegesProducer {

    /**
     *
     * @param schema - Database schema
     * @param table - Database table
     * @param user  - Database user (grantee)
     * @param privileges - List of table privileges
     * @return
     */
    public String produce(String schema, String table, String user, List<String> privileges) {
        validateParameters(schema, table, user, privileges);
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
        sb.append(" ON ");
        if (schema != null) {
            sb.append(schema);
            sb.append(".");
        }
        sb.append("\"");
        sb.append(table);
        sb.append("\"");
        sb.append(" TO \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private void validateParameters(String schema , String table, String user, List<String> privileges) {
        if (schema != null && schema.trim().isEmpty())
        {
            throw new IllegalArgumentException("schema cannot be blank");
        }
        if (table == null)
        {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (table.trim().isEmpty())
        {
            throw new IllegalArgumentException("Table name cannot be blank");
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
