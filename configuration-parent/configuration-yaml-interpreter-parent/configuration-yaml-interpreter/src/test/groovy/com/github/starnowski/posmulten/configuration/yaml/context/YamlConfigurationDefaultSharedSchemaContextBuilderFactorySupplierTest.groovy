package com.github.starnowski.posmulten.configuration.yaml.context

import spock.lang.Specification

import static java.util.Arrays.asList

class YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest extends Specification {

    def tested = new YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier()

    def "should return priority with value zero"()
    {
        expect:
            tested.getPriority() == 0
    }

    def "should return extensions related to YAML language"()
    {
        given:
            def expectedExtensions = asList("yaml", "yml")

        when:
            def results = tested.getSupportedFileExtensions()

        then:
            results
            results.size() == expectedExtensions.size()
            results.containsAll(expectedExtensions)
    }

    def "should return factory supplier"()
    {
        when:
            def supplier = tested.getFactorySupplier()

        then:
            supplier

        and: "supplier creates object of correct type"
            YamlConfigurationDefaultSharedSchemaContextBuilderFactory.class.equals(supplier.get().getClass())
    }
}
