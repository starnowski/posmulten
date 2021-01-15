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
