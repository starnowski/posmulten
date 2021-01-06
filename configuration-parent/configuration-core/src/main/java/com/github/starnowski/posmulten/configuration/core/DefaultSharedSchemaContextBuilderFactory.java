package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class DefaultSharedSchemaContextBuilderFactory {

    private final DefaultSharedSchemaContextBuilderConfigurationEnricher defaultSharedSchemaContextBuilderConfigurationEnricher;
    private final DefaultSharedSchemaContextBuilderConfigurationInitializingBean defaultSharedSchemaContextBuilderConfigurationInitializingBean;

    public DefaultSharedSchemaContextBuilderFactory()
    {
        this(new DefaultSharedSchemaContextBuilderConfigurationEnricher(), new DefaultSharedSchemaContextBuilderConfigurationInitializingBean());
    }

    public DefaultSharedSchemaContextBuilderFactory(DefaultSharedSchemaContextBuilderConfigurationEnricher defaultSharedSchemaContextBuilderConfigurationEnricher, DefaultSharedSchemaContextBuilderConfigurationInitializingBean defaultSharedSchemaContextBuilderConfigurationInitializingBean) {
        this.defaultSharedSchemaContextBuilderConfigurationEnricher = defaultSharedSchemaContextBuilderConfigurationEnricher;
        this.defaultSharedSchemaContextBuilderConfigurationInitializingBean = defaultSharedSchemaContextBuilderConfigurationInitializingBean;
    }

    public DefaultSharedSchemaContextBuilder build(SharedSchemaContextConfiguration contextConfiguration)
    {
        DefaultSharedSchemaContextBuilder builder = defaultSharedSchemaContextBuilderConfigurationInitializingBean.produce(contextConfiguration);
        return defaultSharedSchemaContextBuilderConfigurationEnricher.enrich(builder, contextConfiguration);
    }

    public DefaultSharedSchemaContextBuilderConfigurationEnricher getDefaultSharedSchemaContextBuilderConfigurationEnricher() {
        return defaultSharedSchemaContextBuilderConfigurationEnricher;
    }

    public DefaultSharedSchemaContextBuilderConfigurationInitializingBean getDefaultSharedSchemaContextBuilderConfigurationInitializingBean() {
        return defaultSharedSchemaContextBuilderConfigurationInitializingBean;
    }
}
