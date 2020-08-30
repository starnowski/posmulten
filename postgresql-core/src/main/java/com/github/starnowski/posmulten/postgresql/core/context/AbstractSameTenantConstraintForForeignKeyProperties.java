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
 * Type contains information that are required to the creation of constraint that checks if foreign key in the main table refers to record
 * that exists in the foreign table and which belongs to the current tenant.
 */
public interface AbstractSameTenantConstraintForForeignKeyProperties {

    /**
     * The method returns map contains information about which foreign key column refers to specific primary key column.
     * The foreign key column is the map key and the primary key column is its value.
     * @return map contains information about which foreign key column refers to specific primary key column. The foreign key column is the map key and the primary key column is its value.
     */
    Map<String, String> getForeignKeyPrimaryKeyColumnsMappings();

    /**
     * The method returns the name of the constraint.
     * @return name of the constraint
     */
    String getConstraintName();
}
