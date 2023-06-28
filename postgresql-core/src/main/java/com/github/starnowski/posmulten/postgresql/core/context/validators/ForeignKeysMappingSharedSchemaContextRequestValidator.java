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

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.IncorrectForeignKeysMappingException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableException;
import com.github.starnowski.posmulten.postgresql.core.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ForeignKeysMappingSharedSchemaContextRequestValidator implements ISharedSchemaContextRequestValidator{
    @Override
    public void validate(SharedSchemaContextRequest request) throws IncorrectForeignKeysMappingException, MissingRLSPolicyDeclarationForTableException {
        List<Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>> constraintsRequests = request.getSameTenantConstraintForForeignKeyProperties().entrySet().stream().map(entry -> new Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>(entry.getKey(), entry.getValue())).collect(toList());
        Map<TableKey, ITableColumns> primaryKeyDefinitions = request.getTableColumnsList();
        for (Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties> constrainRequest : constraintsRequests)
        {
            Set<String> primaryKeysInForeignKeysMapping = constrainRequest.getValue().getForeignKeyPrimaryKeyColumnsMappings().values().stream().collect(Collectors.toSet());
            ITableColumns rlsPolicy = primaryKeyDefinitions.get(constrainRequest.getKey().getForeignKeyTable());
            if (rlsPolicy == null)
            {
                TableKey tableKey = constrainRequest.getKey().getForeignKeyTable();
                throw new MissingRLSPolicyDeclarationForTableException(tableKey, format("Missing RLS policy declaration for table %1$s in schema %2$s", tableKey.getTable(), tableKey.getSchema()));
            }
            Set<String> primaryKeysForRLSPolicy = rlsPolicy.getIdentityColumnNameAndTypeMap().keySet();
            if (!primaryKeysInForeignKeysMapping.equals(primaryKeysForRLSPolicy))
            {
                throw new IncorrectForeignKeysMappingException(prepareExceptionMessage(constrainRequest.getKey().getMainTable(), constrainRequest.getKey().getForeignKeyTable(), primaryKeysInForeignKeysMapping, primaryKeysForRLSPolicy),
                        constrainRequest.getKey().getMainTable(),
                        constrainRequest.getKey().getForeignKeyTable(),
                        primaryKeysInForeignKeysMapping,
                        primaryKeysForRLSPolicy);
            }
        }
    }

    private String prepareExceptionMessage(TableKey foreignTableKey, TableKey primaryTableKey, Set<String> foreignKeys, Set<String> primaryKeys)
    {
        return format("There is mismatch between foreign keys column mapping (%1$s) in %2$s table and primary keys column declaration (%3$s) for %4$s table",
                foreignKeys.stream().sorted().collect(joining(", ")),
                returnTableName(foreignTableKey),
                primaryKeys.stream().sorted().collect(joining(", ")),
                returnTableName(primaryTableKey));
    }

    private String returnTableName(TableKey tableKey)
    {
        return (tableKey.getSchema() == null ? "" : tableKey.getSchema() + ".") + tableKey.getTable();
    }
}
