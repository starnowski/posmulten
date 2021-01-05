package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class DefaultSharedSchemaContextBuilderConfigurationInitializingBean {

    public DefaultSharedSchemaContextBuilder produce(SharedSchemaContextConfiguration contextConfiguration)
    {
        return new DefaultSharedSchemaContextBuilder(contextConfiguration.getDefaultSchema());
    }
}
