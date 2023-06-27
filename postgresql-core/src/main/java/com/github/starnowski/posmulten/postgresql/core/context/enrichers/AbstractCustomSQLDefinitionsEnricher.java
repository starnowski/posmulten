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

import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import java.util.Objects;

public abstract class AbstractCustomSQLDefinitionsEnricher implements ISharedSchemaContextEnricher {

    private final CustomSQLDefinitionPairPositionProvider customSQLDefinitionPairPositionProvider;

    protected AbstractCustomSQLDefinitionsEnricher(CustomSQLDefinitionPairPositionProvider customSQLDefinitionPairPositionProvider) {
        this.customSQLDefinitionPairPositionProvider = customSQLDefinitionPairPositionProvider;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        request.getCustomSQLDefinitionPairs().stream().filter(customSQLDefinitionPair -> Objects.equals(customSQLDefinitionPair.getPosition(), customSQLDefinitionPairPositionProvider.getPosition()))
                .map(pair -> pair.getSqlDefinition())
                .forEach(sqlDefinition -> context.addSQLDefinition(sqlDefinition));
        return context;
    }
}
