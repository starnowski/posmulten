package com.github.starnowski.posmulten.configuration.core.context;

import com.github.starnowski.posmulten.configuration.core.DefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public abstract class AbstractDefaultSharedSchemaContextBuilderFactory implements IDefaultSharedSchemaContextBuilderFactory{

    private final DefaultSharedSchemaContextBuilderFactory defaultSharedSchemaContextBuilderFactory = new DefaultSharedSchemaContextBuilderFactory();

    @Override
    public DefaultSharedSchemaContextBuilder build(String filePath) {
        SharedSchemaContextConfiguration contextConfiguration = prepareConfigurationBasedOnFile(filePath);
        return defaultSharedSchemaContextBuilderFactory.build(contextConfiguration);
    }

    protected abstract SharedSchemaContextConfiguration prepareConfigurationBasedOnFile(String filePath);
}
