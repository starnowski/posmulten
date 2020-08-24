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

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public class IsRecordBelongsToCurrentTenantConstraintProducer {

    public SQLDefinition produce(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        validate(parameters);
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters));
    }

    protected String prepareCreateScript(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ALTER TABLE ");
        stringBuilder.append(prepareTableReference(parameters));
        stringBuilder.append(" ADD CONSTRAINT ");
        stringBuilder.append(parameters.getConstraintName());
        stringBuilder.append(" CHECK ");
        stringBuilder.append("(");
        stringBuilder.append("(");
        stringBuilder.append(parameters.getPrimaryColumnsValuesMap().entrySet().stream().map(Map.Entry::getValue).map(FunctionArgumentValueToStringMapper::mapToString).sorted().map(s -> s + " IS NULL").collect(joining(" AND ")));
        stringBuilder.append(")");
        stringBuilder.append(" OR ");
        stringBuilder.append("(");
        stringBuilder.append(parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory().returnIsRecordBelongsToCurrentTenantFunctionInvocation(parameters.getPrimaryColumnsValuesMap()));
        stringBuilder.append(")");
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    protected void validate(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        if (parameters.getTableName() == null)
        {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (parameters.getTableName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Table name cannot be empty");
        }
        if (parameters.getTableSchema() != null && parameters.getTableSchema().trim().isEmpty())
        {
            throw new IllegalArgumentException("Table schema cannot be empty");
        }
        if (parameters.getConstraintName() == null)
        {
            throw new IllegalArgumentException("Constraint name cannot be null");
        }
        if (parameters.getConstraintName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Constraint name cannot be empty");
        }
        if (parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("Object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory cannot be null");
        }
    }

    private String prepareTableReference(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if (parameters.getTableSchema() != null)
        {
            stringBuilder.append("\"");
            stringBuilder.append(parameters.getTableSchema());
            stringBuilder.append("\"");
            stringBuilder.append(".");
        }
        stringBuilder.append("\"");
        stringBuilder.append(parameters.getTableName());
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }

    protected String prepareDropScript(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ALTER TABLE ");
        stringBuilder.append(prepareTableReference(parameters));
        stringBuilder.append(" DROP CONSTRAINT IF EXISTS ");
        stringBuilder.append(parameters.getConstraintName());
        stringBuilder.append(";");
        return stringBuilder.toString();
    }
}
