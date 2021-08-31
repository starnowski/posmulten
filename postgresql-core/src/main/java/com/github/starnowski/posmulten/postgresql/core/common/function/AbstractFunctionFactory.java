/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class AbstractFunctionFactory<P extends IFunctionFactoryParameters, R extends DefaultFunctionDefinition> implements FunctionFactory<P, R> {

    @Override
    public R produce(P parameters) {
        validate(parameters);
        String createScript = produceStatement(parameters);
        String functionReference = returnFunctionReference(parameters);
        String dropScript = returnDropScript(parameters);
        return returnFunctionDefinition(parameters, new FunctionDefinitionBuilder()
                .withCreateScript(createScript)
                .withFunctionReference(functionReference)
                .withDropScript(dropScript)
                .withCheckingStatements(returnCheckingStatements(parameters))
                .withFunctionArguments(prepareFunctionArguments(parameters))
                .build());
    }

    protected String returnFunctionReference(P parameters) {
        StringBuilder sb = new StringBuilder();
        if (parameters.getSchema() != null) {
            sb.append(parameters.getSchema());
            sb.append(".");
        }
        sb.append(parameters.getFunctionName());
        return sb.toString();
    }

    protected String returnDropScript(P parameters) {
        List<IFunctionArgument> arguments = prepareFunctionArguments(parameters);
        return format("DROP FUNCTION IF EXISTS %s(%s);", returnFunctionReference(parameters), prepareArgumentsPhrase(arguments));
    }

    protected List<String> returnCheckingStatements(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(1) FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE ");
        sb.append("pg.proname = '");
        sb.append(parameters.getFunctionName());
        sb.append("' AND ");
        if (parameters.getSchema() == null) {
            sb.append("pgn.nspname = 'public'");
        } else {
            sb.append("pgn.nspname = '");
            sb.append(parameters.getSchema());
            sb.append("'");
        }
        sb.append(" AND ");
        sb.append("pg.pronamespace =  pgn.oid");
        return singletonList(sb.toString());
    }

    protected String prepareArgumentsPhrase(List<IFunctionArgument> functionArguments) {
        return functionArguments.stream().map(IFunctionArgument::getType).collect(Collectors.joining(", "));
    }

    protected void validate(P parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        if (parameters.getFunctionName() == null) {
            throw new IllegalArgumentException("Function name cannot be null");
        }
        if (parameters.getFunctionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be blank");
        }
        if (parameters.getSchema() != null && parameters.getSchema().trim().isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
    }

    abstract protected R returnFunctionDefinition(P parameters, IFunctionDefinition functionDefinition);

    abstract protected String produceStatement(P parameters);

    protected List<IFunctionArgument> prepareFunctionArguments(P parameters) {
        return emptyList();
    }
}
