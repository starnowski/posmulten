package com.github.starnowski.posmulten.configuration

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier
import spock.lang.Specification
import spock.lang.Unroll

class DefaultSharedSchemaContextBuilderFactoryResolverTest extends Specification {

    @Unroll
    def "should use specific components with correct order and pass correct parameters: loaded suppliers #loadedSuppliers, custom suppliers #customSuppliers, file path #filePath"()
    {
        given:
            Set<IDefaultSharedSchemaContextBuilderFactorySupplier> expectedSuppliers = new HashSet<>()
            loadedSuppliers.forEach({it -> expectedSuppliers.add(it)})
            customSuppliers.forEach({it -> expectedSuppliers.add(it)})
            IDefaultSharedSchemaContextBuilderFactory expectedResult = Mock(IDefaultSharedSchemaContextBuilderFactory)
            def defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher = Mock(DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher)
            def defaultSharedSchemaContextBuilderFactorySupplierResolver = Mock(DefaultSharedSchemaContextBuilderFactorySupplierResolver)
            def context = Mock(DefaultSharedSchemaContextBuilderFactoryResolverContext)
            context.getDefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher() >> defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher
            context.getDefaultSharedSchemaContextBuilderFactorySupplierResolver() >> defaultSharedSchemaContextBuilderFactorySupplierResolver
            context.getSuppliers() >> customSuppliers
            def tested = new DefaultSharedSchemaContextBuilderFactoryResolver(context)

        when:
            def actualResult = tested.resolve(filePath)

        then:
            actualResult == expectedResult

        then:
            1 * defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher.findDefaultSharedSchemaContextBuilderFactorySuppliers() >> loadedSuppliers

        then:
            1 * defaultSharedSchemaContextBuilderFactorySupplierResolver.resolveSupplierBasedOnPriorityForFile(filePath, expectedSuppliers) >> expectedResult

        where:
            filePath        |   loadedSuppliers                                     |   customSuppliers
            "ASDF.xml"      |   [loadedSupplier("x1"), loadedSupplier("47")]        |   [customSupplier("123x")]
            "somefile"      |   [loadedSupplier("files")]                           |   [customSupplier("ee6"), customSupplier("json"), customSupplier("659")]
            "jsonfilepath"  |   []                                                  |   [customSupplier("ee6"), customSupplier("json"), customSupplier("659")]
            "zxvzv"         |   [loadedSupplier("files")]                           |   []
    }

    private AbstractDefaultSharedSchemaContextBuilderFactorySupplier loadedSupplier(String name)
    {
        def result = Mock(AbstractDefaultSharedSchemaContextBuilderFactorySupplier)
        result.toString() >> name
        result
    }

    private IDefaultSharedSchemaContextBuilderFactorySupplier customSupplier(String name)
    {
        def result = Mock(IDefaultSharedSchemaContextBuilderFactorySupplier)
        result.toString() >> name
        result
    }
}
