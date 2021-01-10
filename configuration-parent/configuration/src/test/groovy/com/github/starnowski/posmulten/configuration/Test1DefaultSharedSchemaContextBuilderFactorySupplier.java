package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;

import java.util.List;
import java.util.function.Supplier;

public class Test1DefaultSharedSchemaContextBuilderFactorySupplier extends AbstractDefaultSharedSchemaContextBuilderFactorySupplier {
    @Override
    public Supplier<IDefaultSharedSchemaContextBuilderFactory> getFactorySupplier() {
        return null;
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return null;
    }
}
