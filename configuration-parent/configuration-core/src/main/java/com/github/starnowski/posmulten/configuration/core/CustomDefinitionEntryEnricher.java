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
package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry;
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition;
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomDefinitionEntryEnricher {

    private final static Map<String, CustomSQLDefinitionPairPositionProvider> defaultPositionsMap = Stream.of(CustomSQLDefinitionPairDefaultPosition.values()).collect(Collectors.toMap(Enum::name, e -> e));

    public void enrich(DefaultSharedSchemaContextBuilder builder, CustomDefinitionEntry cd) {
        CustomSQLDefinitionPairPositionProvider provider = defaultPositionsMap.containsKey(cd.getPosition().name()) ? defaultPositionsMap.get(cd.getPosition().name()) : new CustomPositionProvider(cd.getCustomPosition());
        builder.addCustomSQLDefinition(provider, cd.getCreationScript(), cd.getDropScript(), cd.getValidationScripts());
    }

    private static final class CustomPositionProvider implements CustomSQLDefinitionPairPositionProvider {

        private final String position;

        public CustomPositionProvider(String position) {
            this.position = position;
        }

        @Override
        public String getPosition() {
            return this.position;
        }
    }
}
