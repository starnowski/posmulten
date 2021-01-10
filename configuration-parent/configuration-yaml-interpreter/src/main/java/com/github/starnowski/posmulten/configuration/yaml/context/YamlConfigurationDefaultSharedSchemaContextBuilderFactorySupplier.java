package com.github.starnowski.posmulten.configuration.yaml.context;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier extends AbstractDefaultSharedSchemaContextBuilderFactorySupplier {
    @Override
    public Supplier<IDefaultSharedSchemaContextBuilderFactory> getFactorySupplier() {
        return YamlConfigurationDefaultSharedSchemaContextBuilderFactory::new;
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return asList("yaml", "yml");
    }
}
