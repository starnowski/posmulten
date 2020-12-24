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

import com.github.starnowski.posmulten.postgresql.core.rls.function.*;

import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.rls.function.IIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantFunctionDefinitionProducer {

    private IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();

    public IsRecordBelongsToCurrentTenantFunctionDefinition produce(TableKey tableKey, String tenantColumnName, Map<String, String> identityColumnNameAndTypeMa, IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory, String functionName, String schema)
    {
        IIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(schema)
                .withFunctionName(functionName)
                .withRecordTableName(tableKey.getTable())
                .withRecordSchemaName(tableKey.getSchema())
                .withiGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
                .withTenantColumn(tenantColumnName)
                .withKeyColumnsPairsList(identityColumnNameAndTypeMa.entrySet().stream().map(entry -> pairOfColumnWithType(entry.getKey(), entry.getValue())).collect(toList())).build();
        return isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
    }

    void setIsRecordBelongsToCurrentTenantProducer(IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer) {
        this.isRecordBelongsToCurrentTenantProducer = isRecordBelongsToCurrentTenantProducer;
    }
}
