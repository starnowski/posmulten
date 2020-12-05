/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

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
     * @return object of type {@link SQLDefinition}
     */
    public SQLDefinition produce(String schema, String table, String user, List<String> privileges) {
        validateParameters(schema, table, user, privileges);
        return new DefaultSQLDefinition(prepareCreateScript(schema, table, user, privileges), prepareDropScript(schema, table, user, privileges));
    }

    private String prepareDropScript(String schema, String table, String user, List<String> privileges) {
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
        sb.append(" ON ");
        if (schema != null) {
            sb.append(schema);
            sb.append(".");
        }
        sb.append("\"");
        sb.append(table);
        sb.append("\"");
        sb.append(" FROM \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private String prepareCreateScript(String schema, String table, String user, List<String> privileges) {
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
