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

public class SameTenantConstraintForForeignKeyProperties implements AbstractSameTenantConstraintForForeignKeyProperties{

    private final String constraintName;
    private final Map<String, String> foreignKeyPrimaryKeyColumnsMappings;

    public SameTenantConstraintForForeignKeyProperties(String constraintName, Map<String, String> foreignKeyPrimaryKeyColumnsMappings) {
        this.constraintName = constraintName;
        this.foreignKeyPrimaryKeyColumnsMappings = foreignKeyPrimaryKeyColumnsMappings;
    }

    public Map<String, String> getForeignKeyPrimaryKeyColumnsMappings() {
        return foreignKeyPrimaryKeyColumnsMappings;
    }

    public String getConstraintName() {
        return constraintName;
    }
}
