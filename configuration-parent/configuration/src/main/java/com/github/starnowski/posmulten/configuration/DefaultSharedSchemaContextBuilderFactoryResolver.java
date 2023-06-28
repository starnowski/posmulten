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

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.starnowski.posmulten.configuration.DefaultSharedSchemaContextBuilderFactoryResolverContext.builder;
import static java.util.stream.Collectors.toSet;

public class DefaultSharedSchemaContextBuilderFactoryResolver {

    private final DefaultSharedSchemaContextBuilderFactoryResolverContext context;

    public DefaultSharedSchemaContextBuilderFactoryResolver() {
        this.context = builder().build();
    }

    public DefaultSharedSchemaContextBuilderFactoryResolver(DefaultSharedSchemaContextBuilderFactoryResolverContext context) {
        this.context = context;
    }

    public IDefaultSharedSchemaContextBuilderFactory resolve(String filePath) throws NoDefaultSharedSchemaContextBuilderFactorySupplierException
    {
        Set<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers = new HashSet<>();
        Set<AbstractDefaultSharedSchemaContextBuilderFactorySupplier> loadedSuppliers = context.getDefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher().findDefaultSharedSchemaContextBuilderFactorySuppliers();
        if (loadedSuppliers != null)
        {
            suppliers.addAll(loadedSuppliers);
        }
        List<IDefaultSharedSchemaContextBuilderFactorySupplier> customSuppliers = context.getSuppliers();
        if (customSuppliers != null)
        {
            suppliers.addAll(customSuppliers);
        }
        IDefaultSharedSchemaContextBuilderFactorySupplier supplier = context.getDefaultSharedSchemaContextBuilderFactorySupplierResolver().resolveSupplierBasedOnPriorityForFile(filePath, suppliers);
        if (supplier == null)
        {
            throw new NoDefaultSharedSchemaContextBuilderFactorySupplierException(filePath, suppliers.stream().flatMap(sup -> sup.getSupportedFileExtensions().stream()).collect(toSet()));
        }
        return supplier == null ? null : supplier.getFactorySupplier().get() ;
    }
}
