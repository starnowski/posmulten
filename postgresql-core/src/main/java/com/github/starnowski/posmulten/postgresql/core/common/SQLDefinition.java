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
package com.github.starnowski.posmulten.postgresql.core.common;

public interface SQLDefinition {

    /**
     * Returns DDL statement which should be executed to apply changes that are represented by the main object.
     * @return DDL statement which should be executed to apply changes that are represented by the main object.
     */
    String getCreateScript();

    /**
     * Returns DDL statement that drops changes applied by statement returned by the {@link #getCreateScript()} method.
     * <br/>
     * <b>IMPORTANT!</b>
     * <br/>
     * By default, there is no assumption that statement has to contains the compensation operation for operation returned by the {@link #getCreateScript()} method.
     * This means that the operation can not be by default treated as a rollback operation, but an operation that removes changes applied by statement returned by the {@link #getCreateScript()} method.
     * @return DDL statement that drops changes applied by statement returned by the {@link #getCreateScript()} method.
     */
    String getDropScript();
}
