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
import java.util.Set;

/**
 * Type used to identifier request for creation of constraint that checks if foreign key in the main table refers to record
 * that exists in the foreign table and which belongs to the current tenant.
 * @see com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher
 */
public final class SameTenantConstraintForForeignKey {

    /**
     * Table identifier that has foreign key columns.
     */
    private final TableKey mainTable;
    /**
     * Table identifier that has primary key columns.
     */
    private final TableKey foreignKeyTable;
    /**
     * The set of columns names of the foreign key.
     */
    private final Set<String> foreignKeyColumns;

    public SameTenantConstraintForForeignKey(TableKey mainTable, TableKey foreignKeyTable, Set<String> foreignKeyColumns) {
        this.mainTable = mainTable;
        this.foreignKeyTable = foreignKeyTable;
        this.foreignKeyColumns = foreignKeyColumns;
    }

    public TableKey getMainTable() {
        return mainTable;
    }

    public TableKey getForeignKeyTable() {
        return foreignKeyTable;
    }

    public Set<String> getForeignKeyColumns() {
        return foreignKeyColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SameTenantConstraintForForeignKey that = (SameTenantConstraintForForeignKey) o;
        return Objects.equals(mainTable, that.mainTable) &&
                Objects.equals(foreignKeyTable, that.foreignKeyTable) &&
                Objects.equals(foreignKeyColumns, that.foreignKeyColumns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainTable, foreignKeyTable, foreignKeyColumns);
    }
}
