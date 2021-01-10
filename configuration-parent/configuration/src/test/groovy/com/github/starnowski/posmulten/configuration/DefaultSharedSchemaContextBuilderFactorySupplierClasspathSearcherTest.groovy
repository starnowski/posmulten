package com.github.starnowski.posmulten.configuration

import spock.lang.Specification
import spock.lang.Unroll

class DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcherTest extends Specification {

    @Unroll
    def "should set with component of types #expectedTypes"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher()

        when:
            def results = tested.findDefaultSharedSchemaContextBuilderFactorySuppliers()

        then:
            results == expectedTypes

        where:
            expectedTypes << [new HashSet<>(Arrays.asList(Test1DefaultSharedSchemaContextBuilderFactorySupplier.class, Test2DefaultSharedSchemaContextBuilderFactorySupplier.class))]
    }
}
