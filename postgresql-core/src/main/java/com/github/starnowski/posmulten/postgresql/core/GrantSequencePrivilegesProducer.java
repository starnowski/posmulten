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

import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.singletonList;

public class GrantSequencePrivilegesProducer {

    public SQLDefinition produce(String schema, String sequence, String user) {
        validateParameters(schema, sequence, user);
        return new DefaultSQLDefinition(prepareCreateScript(schema, sequence, user), prepareDropScript(schema, sequence, user), prepareCheckingStatements(schema, sequence, user));
    }

    private String prepareDropScript(String schema, String sequence, String user) {
        StringBuilder sb = new StringBuilder();
        sb.append("REVOKE ALL PRIVILEGES ON SEQUENCE ");
        if (schema == null) {
            sb.append("public");
        } else {
            sb.append(schema);
        }
        sb.append(".");
        sb.append("\"");
        sb.append(sequence);
        sb.append("\"");
        sb.append(" FROM \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private String prepareCreateScript(String schema, String sequence, String user) {
        StringBuilder sb = new StringBuilder();
        sb.append("GRANT ALL PRIVILEGES ON SEQUENCE ");
        if (schema == null) {
            sb.append("public");
        } else {
            sb.append(schema);
        }
        sb.append(".");
        sb.append("\"");
        sb.append(sequence);
        sb.append("\"");
        sb.append(" TO \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private List<String> prepareCheckingStatements(String schema, String sequence, String user) {
        String pattern = "SELECT COUNT(1) FROM  information_schema.usage_privileges WHERE  grantee = '%1s' AND object_schema = '%2s' AND object_name = '%3s' AND object_type = 'SEQUENCE' AND privilege_type = 'USAGE';";
        return singletonList(format(pattern, user, schema, sequence));
    }

    private void validateParameters(String schema, String sequence, String user) {
        if (schema != null && schema.trim().isEmpty()) {
            throw new IllegalArgumentException("schema cannot be blank");
        }
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (user.trim().isEmpty()) {
            throw new IllegalArgumentException("user cannot be blank");
        }
        if (sequence == null) {
            throw new IllegalArgumentException("sequence cannot be null");
        }
        if (sequence.trim().isEmpty()) {
            throw new IllegalArgumentException("sequence cannot be blank");
        }
    }
}
