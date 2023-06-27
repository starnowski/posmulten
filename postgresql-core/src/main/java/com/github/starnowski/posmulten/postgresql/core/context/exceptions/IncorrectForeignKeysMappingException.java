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
package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.Set;

public class IncorrectForeignKeysMappingException extends SharedSchemaContextBuilderException{

    private TableKey foreignTableKey;
    private TableKey primaryTableKey;
    private Set<String> foreignKeys;
    private Set<String> primaryKeys;

    public IncorrectForeignKeysMappingException(String message, TableKey foreignTableKey, TableKey primaryTableKey, Set<String> foreignKeys, Set<String> primaryKeys) {
        super(message);
        this.foreignTableKey = foreignTableKey;
        this.primaryTableKey = primaryTableKey;
        this.foreignKeys = foreignKeys;
        this.primaryKeys = primaryKeys;
    }

    public TableKey getForeignTableKey() {
        return foreignTableKey;
    }

    public TableKey getPrimaryTableKey() {
        return primaryTableKey;
    }

    public Set<String> getForeignKeys() {
        return foreignKeys;
    }

    public Set<String> getPrimaryKeys() {
        return primaryKeys;
    }


}
