package com.github.starnowski.posmulten.configuration

import spock.lang.Specification

class DefaultSharedSchemaContextBuilderFactoryResolverContextTest extends Specification {

    def "should create context object with default components"()
    {
        when:
            def tested = DefaultSharedSchemaContextBuilderFactoryResolverContext.builder().build()

        then:
            tested
            DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher.class == tested.getDefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher().getClass()
            DefaultSharedSchemaContextBuilderFactorySupplierResolver.class == tested.getDefaultSharedSchemaContextBuilderFactorySupplierResolver().getClass()
    }
}
