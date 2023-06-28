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
package com.github.starnowski.posmulten.postgresql.core.context.decorator;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class BasicSharedSchemaContextDecorator extends AbstractSharedSchemaContextDecorator {

    private final Map<String, String> variableValueMap;

    public BasicSharedSchemaContextDecorator(ISharedSchemaContext sharedSchemaContext, BasicSharedSchemaContextDecoratorContext basicSharedSchemaContextDecoratorContext) {
        super(sharedSchemaContext);
        this.variableValueMap = unmodifiableMap(ofNullable(basicSharedSchemaContextDecoratorContext.getReplaceCharactersMap()).orElse(new HashMap<>()));
    }

    Map<String, String> getVariableValueMap() {
        return variableValueMap;
    }

    @Override
    protected String convert(String statement) {
        if (statement == null) {
            return null;
        }
        for (Map.Entry<String, String> entry : variableValueMap.entrySet()) {
            statement = statement.replace(entry.getKey(), entry.getValue());
        }
        return statement;
    }
}
