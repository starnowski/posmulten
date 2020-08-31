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

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer {

    private IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer = new IsRecordBelongsToCurrentTenantConstraintProducer();

    public List<SQLDefinition> produce(IIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters parameters)
    {
        IsRecordBelongsToCurrentTenantConstraintProducerParameters isRecordBelongsToCurrentTenantConstraintProducerParameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(parameters.getConstraintName())
                .withTableName(parameters.getTableKey().getTable())
                .withTableSchema(parameters.getTableKey().getSchema())
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory())
                .withPrimaryColumnsValuesMap(convertForeignPrimaryKeyMapping(parameters.getForeignKeyPrimaryKeyMappings()))
                .build();
        return Collections.singletonList(isRecordBelongsToCurrentTenantConstraintProducer.produce(isRecordBelongsToCurrentTenantConstraintProducerParameters));
    }

    void setIsRecordBelongsToCurrentTenantConstraintProducer(IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer) {
        this.isRecordBelongsToCurrentTenantConstraintProducer = isRecordBelongsToCurrentTenantConstraintProducer;
    }

    private Map<String, FunctionArgumentValue> convertForeignPrimaryKeyMapping(Map<String, String> foreignKeyPrimaryKeyMappings)
    {
        return foreignKeyPrimaryKeyMappings.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry-> forReference(entry.getKey())));
    }
}
