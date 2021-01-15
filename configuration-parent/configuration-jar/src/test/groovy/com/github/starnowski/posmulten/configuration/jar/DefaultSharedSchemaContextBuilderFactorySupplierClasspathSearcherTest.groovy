package com.github.starnowski.posmulten.configuration.jar

import com.github.starnowski.posmulten.configuration.DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher
import com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier
import spock.lang.Specification
import spock.lang.Unroll

class DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcherTest extends Specification {

    @Unroll
    def "should return set that contains supplier of type #expectedSupplierClass that creates factory of type #expectedFactoryClass"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher()

        when:
            def results = tested.findDefaultSharedSchemaContextBuilderFactorySuppliers()

        then:
            results.stream().filter({it -> expectedSupplierClass == it.getClass()}).findFirst().isPresent()

        then:
            results.stream().filter({it -> expectedSupplierClass == it.getClass()}).findFirst().get().getFactorySupplier().get().getClass() == expectedFactoryClass

        where:
            expectedSupplierClass                                                       |   expectedFactoryClass
            YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier.class     |   YamlConfigurationDefaultSharedSchemaContextBuilderFactory.class
    }
}
