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

import java.util.Map;

/**
 * Type contains information about table columns required for creation of row level security policy for table.
 */
public interface AbstractTableColumns {

    /**
     * Method returns name of column that stores tenant identifier.
     * @return name of column that stores tenant identifier
     */
    String getTenantColumnName();

    /**
     * Method returns map of primary key columns and its types in table.
     * The column name is the map key and the column type is its value.
     * @return map of primary key columns and its types in table
     */
    Map<String, String> getIdentityColumnNameAndTypeMap();
}
