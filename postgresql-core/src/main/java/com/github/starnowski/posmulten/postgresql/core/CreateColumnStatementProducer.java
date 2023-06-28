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

import java.util.Collections;
import java.util.List;

public class CreateColumnStatementProducer {

    public SQLDefinition produce(ICreateColumnStatementProducerParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        String table = parameters.getTable();
        String column = parameters.getColumn();
        String columnType = parameters.getColumnType();
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (table.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be blank");
        }
        if (column == null) {
            throw new IllegalArgumentException("Column name cannot be null");
        }
        if (column.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be blank");
        }
        if (columnType == null) {
            throw new IllegalArgumentException("Statement for column type cannot be null");
        }
        if (columnType.trim().isEmpty()) {
            throw new IllegalArgumentException("Statement for column type cannot be blank");
        }
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters), prepareCheckingStatements(parameters));
    }

    private String prepareDropScript(ICreateColumnStatementProducerParameters parameters) {
        String table = parameters.getTable();
        String column = parameters.getColumn();
        String schema = parameters.getSchema();
        String tableReference = schema == null ? table : schema + "." + table;
        return "ALTER TABLE " + tableReference + " DROP COLUMN " + column + ";";
    }

    private String prepareCreateScript(ICreateColumnStatementProducerParameters parameters) {
        String table = parameters.getTable();
        String column = parameters.getColumn();
        String columnType = parameters.getColumnType();
        String schema = parameters.getSchema();
        String tableReference = schema == null ? table : schema + "." + table;
        return "ALTER TABLE " + tableReference + " ADD COLUMN " + column + " " + columnType + ";";
    }

    private List<String> prepareCheckingStatements(ICreateColumnStatementProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) FROM information_schema.columns WHERE ");
        sb.append("table_catalog = 'postgresql_core' AND ");
        if (parameters.getSchema() == null) {
            sb.append("table_schema = 'public'");
        } else {
            sb.append("table_schema = '");
            sb.append(parameters.getSchema());
            sb.append("'");
        }
        sb.append(" AND ");
        sb.append("table_name = '");
        sb.append(parameters.getTable());
        sb.append("' AND ");
        sb.append("column_name = '");
        sb.append(parameters.getColumn());
        sb.append("';");
        return Collections.singletonList(sb.toString());
    }
}
