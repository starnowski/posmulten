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
package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.stream.Collectors.toSet;

public class DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher {

    public Set<AbstractDefaultSharedSchemaContextBuilderFactorySupplier> findDefaultSharedSchemaContextBuilderFactorySuppliers() {
        Reflections reflections = new Reflections("com.github.starnowski.posmulten.configuration");
        Set<Class<? extends AbstractDefaultSharedSchemaContextBuilderFactorySupplier>> types = reflections.getSubTypesOf(AbstractDefaultSharedSchemaContextBuilderFactorySupplier.class)
                .stream().filter(t -> !isAbstract(t.getModifiers())).collect(toSet());
        Set<AbstractDefaultSharedSchemaContextBuilderFactorySupplier> results = new HashSet<>();
        for (Class<? extends AbstractDefaultSharedSchemaContextBuilderFactorySupplier> type : types) {
            try {
                results.add(type.newInstance());
            } catch (Exception e) {
                //TODO Tests - Use class which does not have default constructor
                throw new RuntimeException("Unable to create instance of class with default constructor", e);
            }
        }
        return results;
    }
}
