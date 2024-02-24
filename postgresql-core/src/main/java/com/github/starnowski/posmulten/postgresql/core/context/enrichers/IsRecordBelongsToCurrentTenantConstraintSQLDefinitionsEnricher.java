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
package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingConstraintNameDeclarationForTableException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingIsRecordBelongsToCurrentTenantFunctionInvocationFactoryException;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.util.Pair;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher implements ISharedSchemaContextEnricher {

    private final IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer;

    public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer) {
        this.isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer;
    }

    public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher() {
        this(new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer());
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws MissingConstraintNameDeclarationForTableException, MissingIsRecordBelongsToCurrentTenantFunctionInvocationFactoryException {
        if (TRUE.equals(request.getCreateForeignKeyConstraintWithTenantColumn()) || TRUE.equals(request.getIgnoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant())) {
            return context;
        }
        List<Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>> constrainsRequests = request.getSameTenantConstraintForForeignKeyProperties().entrySet().stream().map(entry -> new Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>(entry.getKey(), entry.getValue())).collect(toList());
        for (Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties> constraintRequest : constrainsRequests)
        {
            SameTenantConstraintForForeignKey key = constraintRequest.getKey();
            ISameTenantConstraintForForeignKeyProperties requestProperties = constraintRequest.getValue();
            if (requestProperties.getConstraintName() == null)
            {
                throw new MissingConstraintNameDeclarationForTableException(key.getMainTable(), key.getForeignKeyColumns(),
                        format("Missing constraint name that in table %1$s and schema %2$s checks  if the foreign key columns (%3$s) refers to records that belong to the same tenant",
                                key.getMainTable().getTable(),
                                key.getMainTable().getSchema(),
                                key.getForeignKeyColumns().stream().sorted().collect(joining(", "))));
            }
            IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory = context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().get(key.getForeignKeyTable());
            if (isRecordBelongsToCurrentTenantFunctionInvocationFactory == null)
            {
                throw new MissingIsRecordBelongsToCurrentTenantFunctionInvocationFactoryException(key.getForeignKeyTable(), format("Missing object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory for table %1$s and schema %2$s",
                        key.getForeignKeyTable().getTable(),
                        key.getForeignKeyTable().getSchema()));
            }
            IIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters parameters = IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder()
                    .withConstraintName(requestProperties.getConstraintName())
                    .withTableKey(key.getMainTable())
                    .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                    .withForeignKeyPrimaryKeyMappings(requestProperties.getForeignKeyPrimaryKeyColumnsMappings())
                    .build();
            isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(parameters).forEach(sqlDefinition -> context.addSQLDefinition(sqlDefinition));
        }
        return context;
    }
}
