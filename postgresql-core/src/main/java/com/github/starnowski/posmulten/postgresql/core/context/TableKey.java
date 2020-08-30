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
package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.Objects;

/**
 * Type represents table object in specific schema.
 */
public final class TableKey {

    /**
     * Name of table.
     */
    private final String table;
    /**
     * Name of schema.
     */
    private final String schema;

    public TableKey(String table, String schema) {
        this.table = table;
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public String getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "TableKey{" +
                "table='" + table + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableKey tableKey = (TableKey) o;
        return Objects.equals(table, tableKey.table) &&
                Objects.equals(schema, tableKey.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, schema);
    }

}
