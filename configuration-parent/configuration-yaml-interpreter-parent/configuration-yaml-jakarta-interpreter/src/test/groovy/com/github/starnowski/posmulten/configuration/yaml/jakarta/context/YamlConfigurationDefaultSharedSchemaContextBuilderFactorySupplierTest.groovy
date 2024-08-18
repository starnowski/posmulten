package com.github.starnowski.posmulten.configuration.yaml.jakarta.context

import com.github.starnowski.posmulten.configuration.common.yaml.context.AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest

class YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest extends AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest<YamlConfigurationDefaultSharedSchemaContextBuilderFactory, YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier> {

    @Override
    protected YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier getYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier() {
        new YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier()
    }

    @Override
    protected Class<YamlConfigurationDefaultSharedSchemaContextBuilderFactory> getYamlConfigurationDefaultSharedSchemaContextBuilderFactoryClass() {
        YamlConfigurationDefaultSharedSchemaContextBuilderFactory.class
    }
}
