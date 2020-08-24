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
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingFunctionNameDeclarationForTableException;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer = new IsRecordBelongsToCurrentTenantFunctionDefinitionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) throws MissingFunctionNameDeclarationForTableException {
        List<TableKey> tableRequiredFunction = request.getSameTenantConstraintForForeignKeyProperties().keySet().stream().map(constraintKey -> constraintKey.getForeignKeyTable()).distinct().collect(toList());
        for (TableKey tableKey : tableRequiredFunction)
        {
            String functionName = request.getFunctionThatChecksIfRecordExistsInTableNames().get(tableKey);
            if (functionName == null)
            {
                throw new MissingFunctionNameDeclarationForTableException(tableKey, format("Missing function name that checks if record exists in table %1$s and schema %2$s", tableKey.getTable(), tableKey.getSchema()));
            }
            IsRecordBelongsToCurrentTenantFunctionDefinition functionDefinition = isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(tableKey, request.getTableColumnsList().get(tableKey), context.getIGetCurrentTenantIdFunctionInvocationFactory(), functionName, request.getDefaultSchema());
            context.addSQLDefinition(functionDefinition);
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(tableKey, functionDefinition);
        }
        return context;
    }

    void setIsRecordBelongsToCurrentTenantFunctionDefinitionProducer(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer) {
        this.isRecordBelongsToCurrentTenantFunctionDefinitionProducer = isRecordBelongsToCurrentTenantFunctionDefinitionProducer;
    }
}
