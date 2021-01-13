package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;

import static com.github.starnowski.posmulten.configuration.DefaultSharedSchemaContextBuilderFactoryResolverContext.builder;

public class DefaultSharedSchemaContextBuilderFactoryResolver {

    private final DefaultSharedSchemaContextBuilderFactoryResolverContext context;

    public DefaultSharedSchemaContextBuilderFactoryResolver() {
        this.context = builder().build();
    }

    public DefaultSharedSchemaContextBuilderFactoryResolver(DefaultSharedSchemaContextBuilderFactoryResolverContext context) {
        this.context = context;
    }

    public IDefaultSharedSchemaContextBuilderFactory resolve(String filePath)
    {
        //TODO
//        context.getDefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher()
//        context.getSuppliers()
//        context.getDefaultSharedSchemaContextBuilderFactorySupplierResolver()
        return null;
    }
}
