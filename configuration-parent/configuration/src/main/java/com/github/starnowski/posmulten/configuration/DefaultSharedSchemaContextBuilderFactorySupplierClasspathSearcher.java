package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher {

    public Set<AbstractDefaultSharedSchemaContextBuilderFactorySupplier> findDefaultSharedSchemaContextBuilderFactorySuppliers()
    {
        Reflections reflections = new Reflections("com.github.starnowski.posmulten.configuration");
        Set<Class<? extends AbstractDefaultSharedSchemaContextBuilderFactorySupplier>> types = reflections.getSubTypesOf(AbstractDefaultSharedSchemaContextBuilderFactorySupplier.class);
        Set<AbstractDefaultSharedSchemaContextBuilderFactorySupplier> results = new HashSet<>();
        for (Class<? extends AbstractDefaultSharedSchemaContextBuilderFactorySupplier> type : types)
        {
            try {
                results.add(type.newInstance());
            } catch (Exception e) {
                //TODO Tests
                throw new RuntimeException("Unable to create instance of class with default constructor", e);
            }
        }
        return results;
    }
}
