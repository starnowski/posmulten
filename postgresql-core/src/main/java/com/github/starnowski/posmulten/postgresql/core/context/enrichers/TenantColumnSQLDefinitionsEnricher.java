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
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableException;

import java.util.Set;

import static java.lang.String.format;

public class TenantColumnSQLDefinitionsEnricher implements ISharedSchemaContextEnricher {

    private SingleTenantColumnSQLDefinitionsProducer singleTenantColumnSQLDefinitionsProducer = new SingleTenantColumnSQLDefinitionsProducer();

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws MissingRLSPolicyDeclarationForTableException {
        Set<TableKey> tableThatRequireCreationOfTheTenantColumn = request.getCreateTenantColumnTableLists();
        if (tableThatRequireCreationOfTheTenantColumn.isEmpty())
        {
            return context;
        }
        String getCurrentTenantIdFunctionInvocation = context.getIGetCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation();
        for (TableKey tableKey: tableThatRequireCreationOfTheTenantColumn)
        {
            ITableColumns tableColumns = request.getTableColumnsList().get(tableKey);
            if (tableColumns == null)
            {
                throw new MissingRLSPolicyDeclarationForTableException(tableKey, format("Missing RLS policy declaration for table %1$s in schema %2$s", tableKey.getTable(), tableKey.getSchema()));
            }
            singleTenantColumnSQLDefinitionsProducer.produce(tableKey, tableColumns, getCurrentTenantIdFunctionInvocation, request.getCurrentTenantIdProperty(), request.getCurrentTenantIdPropertyType())
                .forEach(context::addSQLDefinition);
        }
        return context;
    }

    void setSingleTenantColumnSQLDefinitionsProducer(SingleTenantColumnSQLDefinitionsProducer singleTenantColumnSQLDefinitionsProducer) {
        this.singleTenantColumnSQLDefinitionsProducer = singleTenantColumnSQLDefinitionsProducer;
    }
}
