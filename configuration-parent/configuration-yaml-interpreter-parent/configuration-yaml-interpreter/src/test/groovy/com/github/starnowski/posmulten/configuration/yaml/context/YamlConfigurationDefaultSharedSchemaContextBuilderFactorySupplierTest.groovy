package com.github.starnowski.posmulten.configuration.yaml.context

import com.github.starnowski.posmulten.configuration.common.yaml.context.AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest

class YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest extends AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest<YamlConfigurationDefaultSharedSchemaContextBuilderFactory, YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier> {

    def tested = new YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier()

    @Override
    protected YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier getYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier() {
        new YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier()
    }

    @Override
    protected Class<YamlConfigurationDefaultSharedSchemaContextBuilderFactory> getYamlConfigurationDefaultSharedSchemaContextBuilderFactoryClass() {
        YamlConfigurationDefaultSharedSchemaContextBuilderFactory.class
    }
}
