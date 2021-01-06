package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class DefaultSharedSchemaContextBuilderFactory {

    public DefaultSharedSchemaContextBuilderFactory(DefaultSharedSchemaContextBuilderConfigurationEnricher defaultSharedSchemaContextBuilderConfigurationEnricher, DefaultSharedSchemaContextBuilderConfigurationInitializingBean defaultSharedSchemaContextBuilderConfigurationInitializingBean) {
        this.defaultSharedSchemaContextBuilderConfigurationEnricher = defaultSharedSchemaContextBuilderConfigurationEnricher;
        this.defaultSharedSchemaContextBuilderConfigurationInitializingBean = defaultSharedSchemaContextBuilderConfigurationInitializingBean;
    }

    private final DefaultSharedSchemaContextBuilderConfigurationEnricher defaultSharedSchemaContextBuilderConfigurationEnricher;
    private final DefaultSharedSchemaContextBuilderConfigurationInitializingBean defaultSharedSchemaContextBuilderConfigurationInitializingBean;

    public DefaultSharedSchemaContextBuilder build(SharedSchemaContextConfiguration contextConfiguration)
    {
        //TODO
        return null;
    }
}
