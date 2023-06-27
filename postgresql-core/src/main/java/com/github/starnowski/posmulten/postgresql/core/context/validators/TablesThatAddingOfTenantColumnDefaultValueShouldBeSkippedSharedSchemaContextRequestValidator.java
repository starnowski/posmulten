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
package com.github.starnowski.posmulten.postgresql.core.context.validators;

import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException;

import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

public class TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidator implements ISharedSchemaContextRequestValidator{
    @Override
    public void validate(SharedSchemaContextRequest request) throws MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException {
        if (!request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().isEmpty())
        {
            Set<TableKey> rlsTables = request.getTableColumnsList().keySet();
            Optional<TableKey> tableWithRLSPolicyDeclaration = request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().stream().filter(tableKey -> !rlsTables.contains(tableKey)).findFirst();
            if (tableWithRLSPolicyDeclaration.isPresent())
            {
                TableKey table = tableWithRLSPolicyDeclaration.get();
                throw new MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException(table, format("Missing RLS policy declaration for table %1$s in schema %2$s for which creation of tenant column should be skipped", table.getTable(), table.getSchema()));
            }
        }
    }
}
