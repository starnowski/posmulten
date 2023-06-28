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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

public class SharedSchemaContextDecoratorFactory {

    private final List<ISharedSchemaContextDecoratorFactory> factories;

    public SharedSchemaContextDecoratorFactory() {
        this(singletonList(new BasicSharedSchemaContextDecoratorFactory()));
    }

    public SharedSchemaContextDecoratorFactory(List<ISharedSchemaContextDecoratorFactory> factories) {
        this.factories = Collections.unmodifiableList(Optional.ofNullable(factories).orElse(new ArrayList<>()));
    }

    public List<ISharedSchemaContextDecoratorFactory> getFactories() {
        return factories;
    }

    public ISharedSchemaContextDecorator build(ISharedSchemaContext sharedSchemaContext, DefaultDecoratorContext decoratorContext) {
        ISharedSchemaContextDecorator result = null;
        for (ISharedSchemaContextDecoratorFactory factory : factories) {
            result = factory.build(sharedSchemaContext, decoratorContext);
            sharedSchemaContext = result;
        }
        return result;
    }
}
