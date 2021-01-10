package com.github.starnowski.posmulten.configuration.yaml.context

import spock.lang.Specification

class YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest extends Specification {

    def tested = new YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier()

    def "should return priority with value zero"()
    {
        expect:
            tested.getPriority() == 0
    }
}
