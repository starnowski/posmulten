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
package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.List;

import static java.util.Collections.singletonList;

public class ForceRowLevelSecurityProducer {

    public SQLDefinition produce(String table, String schema) {
        validateParameters(table, schema);
        return new DefaultSQLDefinition(prepareCreateScript(table, schema), prepareDropScript(table, schema), prepareCheckingStatements(table, schema));
    }

    private String prepareDropScript(String table, String schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE ");
        if (schema != null) {
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
        if (schema != null) {
            sb.append(schema);
            sb.append(".");
        }
        sb.append("\"");
        sb.append(table);
        sb.append("\"");
        sb.append(" FORCE ROW LEVEL SECURITY;");
        return sb.toString();
    }

    private List<String> prepareCheckingStatements(String table, String schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) FROM pg_class pc, pg_catalog.pg_namespace pg ");
        sb.append("WHERE");
        sb.append(" pc.relname = '");
        sb.append(table);
        sb.append("' AND pc.relnamespace = pg.oid AND pg.nspname = '");
        if (schema == null)
            sb.append("public");
        else
            sb.append(schema);
        sb.append("' AND pc.relforcerowsecurity = 't';");
        return singletonList(sb.toString());
    }

    private void validateParameters(String table, String schema) {
        if (schema != null && schema.trim().isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (table.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be blank");
        }
    }
}
