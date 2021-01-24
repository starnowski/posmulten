package com.github.starnowski.posmulten.configuration

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Supplier

import static java.util.Arrays.asList

class DefaultSharedSchemaContextBuilderFactoryResolverTest extends Specification {

    @Unroll
    def "should use specific components and pass correct parameters: loaded suppliers #loadedSuppliers, custom suppliers #customSuppliers, file path #filePath"()
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
            IDefaultSharedSchemaContextBuilderFactorySupplier result = Mock(IDefaultSharedSchemaContextBuilderFactorySupplier)
            result.getFactorySupplier() >> new Supplier<IDefaultSharedSchemaContextBuilderFactory>(){

                @Override
                IDefaultSharedSchemaContextBuilderFactory get() {
                    expectedResult
                }
            }

        when:
            def actualResult = tested.resolve(filePath)

        then:
            actualResult == expectedResult
            1 * defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher.findDefaultSharedSchemaContextBuilderFactorySuppliers() >> new HashSet<>(loadedSuppliers)
            1 * defaultSharedSchemaContextBuilderFactorySupplierResolver.resolveSupplierBasedOnPriorityForFile(filePath, expectedSuppliers) >> result

        where:
            filePath        |   loadedSuppliers                                     |   customSuppliers
            "ASDF.xml"      |   [loadedSupplier("x1"), loadedSupplier("47")]        |   [customSupplier("123x")]
            "somefile"      |   [loadedSupplier("files")]                           |   [customSupplier("ee6"), customSupplier("json"), customSupplier("659")]
            "jsonfilepath"  |   []                                                  |   [customSupplier("ee6"), customSupplier("json"), customSupplier("659")]
            "zxvzv"         |   [loadedSupplier("files")]                           |   []
    }

    @Unroll
    def "should throw an exception when no supplier found based on file extension: loaded suppliers #loadedSuppliers, custom suppliers #customSuppliers, file path #filePath, expected message: "()
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
            tested.resolve(filePath)

        then:
            1 * defaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher.findDefaultSharedSchemaContextBuilderFactorySuppliers() >> new HashSet<>(loadedSuppliers)
            1 * defaultSharedSchemaContextBuilderFactorySupplierResolver.resolveSupplierBasedOnPriorityForFile(filePath, expectedSuppliers) >> null

        then:
            def exp = thrown(NoDefaultSharedSchemaContextBuilderFactorySupplierException)

        and: "exception message should be correct"
            exp.message == expectedMessage

        where:
            filePath        |   loadedSuppliers                                     |   customSuppliers || expectedMessage
            "ASDF.xml"      |   [loadedSupplier("x1", "json"), loadedSupplier("47", "yml")]        |   [customSupplier("123x", "yaml")] ||  "No supplier was found, able to handle file ASDF.xml. Supported file extensions: json, yaml, yml"
            "somefile"      |   [loadedSupplier("x1", "json"), loadedSupplier("47", "yml")]        |   [customSupplier("123x", "yaml")] ||  "No supplier was found, able to handle file somefile. Supported file extensions: json, yaml, yml"
            "somefile.exe"      |   [loadedSupplier("sup99", "yml")]        |   [customSupplier("123x", "yaml")] ||  "No supplier was found, able to handle file somefile.exe. Supported file extensions: yaml, yml"
    }

    private AbstractDefaultSharedSchemaContextBuilderFactorySupplier loadedSupplier(String name, String... supportedFileExtensions)
    {
        def result = Mock(AbstractDefaultSharedSchemaContextBuilderFactorySupplier)
        result.toString() >> name
        if (supportedFileExtensions != null)
        {
            result.getSupportedFileExtensions() >> asList(supportedFileExtensions)
        }
        result
    }

    private IDefaultSharedSchemaContextBuilderFactorySupplier customSupplier(String name, String... supportedFileExtensions)
    {
        def result = Mock(IDefaultSharedSchemaContextBuilderFactorySupplier)
        result.toString() >> name
        if (supportedFileExtensions != null)
        {
            result.getSupportedFileExtensions() >> asList(supportedFileExtensions)
        }
        result
    }
}
