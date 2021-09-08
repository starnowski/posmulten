/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

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
        return new DefaultSQLDefinition(prepareCreateScript(schema, table, user, privileges), prepareDropScript(schema, table, user, privileges),
                prepareCheckingStatements(schema, table, user, privileges));
    }

    private String prepareDropScript(String schema, String table, String user, List<String> privileges) {
        StringBuilder sb = new StringBuilder();
        sb.append("REVOKE ");
        Iterator<String> it = privileges.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
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
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
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

    private List<String> prepareCheckingStatements(String schema, String table, String user, List<String> privileges) {
        return privileges.stream().flatMap(privilege -> prepareCheckingStatements(user, schema, table, privilege).stream()).collect(toList());
    }

    private List<String> prepareCheckingStatements(String user, String schema, String table, String privilege) {
        String trimValue = privilege.trim();
        switch (trimValue) {
            case "ALL":
            case "ALL PRIVILEGES":
                return Arrays.asList(prepareCheckingStatement(user, schema, table, "SELECT"),
                        prepareCheckingStatement(user, schema, table, "INSERT"),
                        prepareCheckingStatement(user, schema, table, "UPDATE"),
                        prepareCheckingStatement(user, schema, table, "DELETE"),
                        prepareCheckingStatement(user, schema, table, "TRUNCATE"),
                        prepareCheckingStatement(user, schema, table, "REFERENCES"),
                        prepareCheckingStatement(user, schema, table, "TRIGGER")
                );
            default:
                return singletonList(prepareCheckingStatement(user, schema, table, trimValue));
        }
    }

    private String prepareCheckingStatement(String user, String schema, String table, String privilege) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) ");
        sb.append("FROM   information_schema.table_privileges ");
        sb.append(" WHERE ");
        sb.append(" table_schema = '");
        if (schema == null || schema.trim().isEmpty()) {
            sb.append("public");
        } else {
            sb.append(schema);
        }
        sb.append("' AND ");
        sb.append(" table_name = '");
        sb.append(table);
        sb.append("' AND ");
        sb.append(" privilege_type = '");
        sb.append(privilege);
        sb.append("' AND ");
        sb.append(" grantee = '");
        sb.append(user);
        sb.append("'");
        return sb.toString();
    }

    private void validateParameters(String schema, String table, String user, List<String> privileges) {
        if (schema != null && schema.trim().isEmpty()) {
            throw new IllegalArgumentException("schema cannot be blank");
        }
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (table.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be blank");
        }
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (user.trim().isEmpty()) {
            throw new IllegalArgumentException("user cannot be blank");
        }
        if (privileges == null) {
            throw new IllegalArgumentException("privileges list cannot be null");
        }
        if (privileges.isEmpty()) {
            throw new IllegalArgumentException("privileges list cannot be empty");
        }
    }

}
