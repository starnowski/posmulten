package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.starnowski.posmulten.configuration.DefaultSharedSchemaContextBuilderFactoryResolverContext.builder;

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
        return supplier == null ? null : supplier.getFactorySupplier().get() ;
    }
}
