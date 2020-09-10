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
package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public class IsRecordBelongsToCurrentTenantConstraintProducer extends AbstractConstraintProducer<IsRecordBelongsToCurrentTenantConstraintProducerParameters> {

    @Override
    protected String prepareConstraintBody(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(parameters.getPrimaryColumnsValuesMap().entrySet().stream().map(Map.Entry::getValue).map(FunctionArgumentValueToStringMapper::mapToString).sorted().map(s -> s + " IS NULL").collect(joining(" AND ")));
        stringBuilder.append(")");
        stringBuilder.append(" OR ");
        stringBuilder.append("(");
        stringBuilder.append(parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory().returnIsRecordBelongsToCurrentTenantFunctionInvocation(parameters.getPrimaryColumnsValuesMap()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    protected void validate(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        super.validate(parameters);
        if (parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("Object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory cannot be null");
        }
    }
}
