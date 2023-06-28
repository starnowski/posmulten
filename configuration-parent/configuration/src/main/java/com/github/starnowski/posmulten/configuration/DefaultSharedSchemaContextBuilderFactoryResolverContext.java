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
package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class DefaultSharedSchemaContextBuilderFactoryResolverContext {

    private final DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher;
    private final DefaultSharedSchemaContextBuilderFactorySupplierResolver defaultSharedSchemaContextBuilderFactorySupplierResolver;
    private final List<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers;

    private DefaultSharedSchemaContextBuilderFactoryResolverContext(DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher, DefaultSharedSchemaContextBuilderFactorySupplierResolver defaultSharedSchemaContextBuilderFactorySupplierResolver, List<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers) {
        this.defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher = defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher;
        this.defaultSharedSchemaContextBuilderFactorySupplierResolver = defaultSharedSchemaContextBuilderFactorySupplierResolver;
        this.suppliers = suppliers;
    }

    public DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher getDefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher() {
        return defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher;
    }

    public DefaultSharedSchemaContextBuilderFactorySupplierResolver getDefaultSharedSchemaContextBuilderFactorySupplierResolver() {
        return defaultSharedSchemaContextBuilderFactorySupplierResolver;
    }

    public List<IDefaultSharedSchemaContextBuilderFactorySupplier> getSuppliers() {
        return suppliers;
    }

    public static DefaultSharedSchemaContextBuilderFactoryResolverContextBuilder builder()
    {
        return new DefaultSharedSchemaContextBuilderFactoryResolverContextBuilder();
    }

    public static class DefaultSharedSchemaContextBuilderFactoryResolverContextBuilder
    {
        private DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher = new DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher();
        private DefaultSharedSchemaContextBuilderFactorySupplierResolver defaultSharedSchemaContextBuilderFactorySupplierResolver = new DefaultSharedSchemaContextBuilderFactorySupplierResolver();
        private List<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers = new ArrayList<>();

        public DefaultSharedSchemaContextBuilderFactoryResolverContextBuilder setDefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher(DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher) {
            this.defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher = defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher;
            return this;
        }

        public DefaultSharedSchemaContextBuilderFactoryResolverContextBuilder setDefaultSharedSchemaContextBuilderFactorySupplierResolver(DefaultSharedSchemaContextBuilderFactorySupplierResolver defaultSharedSchemaContextBuilderFactorySupplierResolver) {
            this.defaultSharedSchemaContextBuilderFactorySupplierResolver = defaultSharedSchemaContextBuilderFactorySupplierResolver;
            return this;
        }

        public DefaultSharedSchemaContextBuilderFactoryResolverContextBuilder registerSupplier(Supplier<IDefaultSharedSchemaContextBuilderFactory> factorySupplier, int priority, String... extensions)
        {
            suppliers.add(new IDefaultSharedSchemaContextBuilderFactorySupplier(){
                @Override
                public Supplier<IDefaultSharedSchemaContextBuilderFactory> getFactorySupplier() {
                    return factorySupplier;
                }

                @Override
                public int getPriority() {
                    return priority;
                }

                @Override
                public List<String> getSupportedFileExtensions() {
                    return extensions == null ? new ArrayList<>() : asList(extensions);
                }
            });
            return this;
        }

        public DefaultSharedSchemaContextBuilderFactoryResolverContext build()
        {
            return new DefaultSharedSchemaContextBuilderFactoryResolverContext(defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher, defaultSharedSchemaContextBuilderFactorySupplierResolver, suppliers);
        }
    }


}
