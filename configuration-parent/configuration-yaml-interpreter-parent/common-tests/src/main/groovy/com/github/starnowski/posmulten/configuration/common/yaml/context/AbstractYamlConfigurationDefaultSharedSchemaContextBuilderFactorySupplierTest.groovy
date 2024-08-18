package com.github.starnowski.posmulten.configuration.common.yaml.context

import com.github.starnowski.posmulten.configuration.yaml.core.context.AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.configuration.yaml.core.context.AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier
import spock.lang.Specification

import static java.util.Arrays.asList

abstract class AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplierTest<F extends AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactory, T extends AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier<F>> extends Specification {

    protected T tested = getYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier()

    protected abstract T getYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier();
    protected abstract Class<F> getYamlConfigurationDefaultSharedSchemaContextBuilderFactoryClass();

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
            getYamlConfigurationDefaultSharedSchemaContextBuilderFactoryClass().equals(supplier.get().getClass())
    }
}
