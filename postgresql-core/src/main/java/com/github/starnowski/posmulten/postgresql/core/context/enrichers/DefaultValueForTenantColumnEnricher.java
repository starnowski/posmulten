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

import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer;
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DefaultValueForTenantColumnEnricher implements ISharedSchemaContextEnricher {

    private final SetDefaultStatementProducer producer;

    public DefaultValueForTenantColumnEnricher() {
        this(new SetDefaultStatementProducer());
    }

    public DefaultValueForTenantColumnEnricher(SetDefaultStatementProducer producer) {
        this.producer = producer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) {
        List<TableKey> tableKeys = emptyList();
        if (request.isCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables()) {
            tableKeys = request.getTableColumnsList().entrySet().stream().filter(entry -> !request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().contains(entry.getKey())).map(entry -> entry.getKey()).collect(toList());
        } else if (!request.getCreateTenantColumnTableLists().isEmpty()) {
            tableKeys = request.getTableColumnsList().entrySet().stream().filter(entry -> request.getCreateTenantColumnTableLists().contains(entry.getKey())).filter(entry -> !request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().contains(entry.getKey())).map(entry -> entry.getKey()).collect(toList());
        }
        if (!tableKeys.isEmpty()) {
            String defaultTenantColumnValue = context.getIGetCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation();
            tableKeys.forEach(tableKey -> context.addSQLDefinition(producer.produce(new SetDefaultStatementProducerParameters(tableKey.getTable(), request.resolveTenantColumnByTableKey(tableKey), defaultTenantColumnValue, tableKey.getSchema()))));
        }
        return context;
    }
}
