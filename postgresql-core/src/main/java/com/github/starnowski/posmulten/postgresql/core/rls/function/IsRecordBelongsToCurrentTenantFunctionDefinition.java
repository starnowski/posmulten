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
package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.*;
import com.github.starnowski.posmulten.postgresql.core.util.Pair;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantFunctionDefinition extends DefaultFunctionDefinition implements IsRecordBelongsToCurrentTenantFunctionInvocationFactory{

    private final List<Pair<String, IFunctionArgument>> keyColumnsPairsList;

    public IsRecordBelongsToCurrentTenantFunctionDefinition(IFunctionDefinition functionDefinition, List<Pair<String, IFunctionArgument>> keyColumnsPairsList) {
        super(functionDefinition);
        this.keyColumnsPairsList = keyColumnsPairsList;
    }

    @Override
    public String returnIsRecordBelongsToCurrentTenantFunctionInvocation(Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        validate(primaryColumnsValuesMap);
        StringBuilder sb = new StringBuilder();
        sb.append(getFunctionReference());
        sb.append("(");
        sb.append(this.keyColumnsPairsList.stream()
                .map(Pair::getKey)
                .map(primaryColumnsValuesMap::get)
                .map(FunctionArgumentValueToStringMapper::mapToString)
                .collect(joining(", ")));
        sb.append(")");
        return sb.toString();
    }

    private void validate(Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        if (primaryColumnsValuesMap == null)
        {
            throw new IllegalArgumentException("The primary columns values map cannot be null");
        }
        if (primaryColumnsValuesMap.isEmpty())
        {
            throw new IllegalArgumentException("The primary columns values map cannot be empty");
        }
        if (keyColumnsPairsList.size() != primaryColumnsValuesMap.size())
        {
            throw new IllegalArgumentException(format("The primary columns values map has invalid size, expected %s elements but has %s elements", keyColumnsPairsList.size(), primaryColumnsValuesMap.size()));
        }
        List<String> missingKeys = keyColumnsPairsList.stream()
                .map(pair -> pair.getKey())
                .filter(key -> !primaryColumnsValuesMap.containsKey(key))
                .collect(toList());
        if (!missingKeys.isEmpty())
        {
            throw new IllegalArgumentException(format("The primary columns values map does not contains keys for function arguments: %s", missingKeys.stream().sorted().collect(joining(", "))));
        }
    }
}
