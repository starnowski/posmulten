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

import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

import java.util.List;

public abstract class ExtendedAbstractFunctionFactory<P extends IFunctionFactoryParameters, R extends DefaultFunctionDefinition> extends AbstractFunctionFactory<P, R> {

    @Override
    protected String produceStatement(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildFunctionNameAndArgumentDeclaration(parameters));
        sb.append(" AS $$");
        sb.append("\n");
        sb.append(buildBodyAndMetaData(parameters));
        sb.append(";");
        return sb.toString();
    }

    protected String buildFunctionNameAndArgumentDeclaration(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE OR REPLACE FUNCTION ");
        sb.append(returnFunctionReference(parameters));
        sb.append(buildArgumentDeclaration(parameters));
        sb.append(" ");
        sb.append(buildReturnPhrase(parameters));
        return sb.toString();
    }

    private String buildReturnPhrase(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("RETURNS ");
        sb.append(prepareReturnType(parameters));
        return sb.toString();
    }

    protected abstract String prepareReturnType(P parameters);

    protected String buildArgumentDeclaration(P parameters) {
        List<IFunctionArgument> arguments = prepareFunctionArguments(parameters);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(prepareArgumentsPhrase(arguments));
        sb.append(")");
        return sb.toString();
    }

    protected String buildBodyAndMetaData(P parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildBody(parameters));
        sb.append("\n");
        sb.append("$$ LANGUAGE ");
        sb.append(returnFunctionLanguage(parameters));
        sb.append("\n");
        sb.append(buildMetaData(parameters));
        return sb.toString();
    }

    protected String buildMetaData(P parameters) {
        MetadataPhraseBuilder metadataPhraseBuilder = new MetadataPhraseBuilder();
        enrichMetadataPhraseBuilder(parameters, metadataPhraseBuilder);
        return metadataPhraseBuilder.build();
    }

    protected abstract void enrichMetadataPhraseBuilder(P parameters, MetadataPhraseBuilder metadataPhraseBuilder);

    protected abstract String buildBody(P parameters);

    protected String returnFunctionLanguage(P parameters) {
        return "sql";
    }
}
