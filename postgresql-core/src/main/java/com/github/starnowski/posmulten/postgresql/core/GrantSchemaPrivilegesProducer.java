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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class GrantSchemaPrivilegesProducer {

    public SQLDefinition produce(String schema, String user, List<String> privileges) {
        validateParameters(schema, user, privileges);
        return new DefaultSQLDefinition(prepareCreateScript(schema, user, privileges), prepareDropScript(schema, user, privileges),
                prepareCheckingStatements(schema, user, privileges));
    }

    private String prepareDropScript(String schema, String user, List<String> privileges) {
        StringBuilder sb = new StringBuilder();
        sb.append("REVOKE ");
        Iterator<String> it = privileges.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
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
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
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

    private List<String> prepareCheckingStatements(String schema, String user, List<String> privileges) {
        return privileges.stream().flatMap(privilege -> prepareCheckingStatements(user, schema, privilege).stream()).collect(toList());
    }

    private List<String> prepareCheckingStatements(String user, String schema, String privilege) {
        String trimValue = privilege.trim();
        switch (trimValue) {
            case "ALL":
            case "ALL PRIVILEGES":
                return Arrays.asList(prepareCheckingStatement(user, schema, "USAGE"), prepareCheckingStatement(user, schema, "CREATE"));
            default:
                return singletonList(prepareCheckingStatement(user, schema, trimValue));
        }
    }

    private String prepareCheckingStatement(String user, String schema, String privilege) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) FROM (");
        sb.append("SELECT pg_catalog.has_schema_privilege('");
        sb.append(user);
        sb.append("', '");
        sb.append(schema);
        sb.append("', '");
        sb.append(privilege);
        sb.append("') AS USER_HAS_PRIVILEGE) AS sub_query_tab_has_schema_privilege");
        sb.append(" WHERE USER_HAS_PRIVILEGE = 't'");
        sb.append(";");
        return sb.toString();
    }

    private void validateParameters(String schema, String user, List<String> privileges) {
        if (schema == null) {
            throw new IllegalArgumentException("Schema name cannot be null");
        }
        if (schema.trim().isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be blank");
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
