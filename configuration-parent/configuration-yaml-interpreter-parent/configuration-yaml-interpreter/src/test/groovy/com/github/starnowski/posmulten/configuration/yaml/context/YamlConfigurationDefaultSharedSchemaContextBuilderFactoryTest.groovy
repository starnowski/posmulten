package com.github.starnowski.posmulten.configuration.yaml.context

import com.github.starnowski.posmulten.configuration.common.yaml.context.AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactoryTest

class YamlConfigurationDefaultSharedSchemaContextBuilderFactoryTest extends AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactoryTest<YamlConfigurationDefaultSharedSchemaContextBuilderFactory> {

    @Override
    protected YamlConfigurationDefaultSharedSchemaContextBuilderFactory getYamlConfigurationDefaultSharedSchemaContextBuilderFactory() {
        new YamlConfigurationDefaultSharedSchemaContextBuilderFactory()
    }
}
