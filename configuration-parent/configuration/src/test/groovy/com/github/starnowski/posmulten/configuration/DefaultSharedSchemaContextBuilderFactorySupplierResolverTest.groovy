package com.github.starnowski.posmulten.configuration

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Arrays.asList

class DefaultSharedSchemaContextBuilderFactorySupplierResolverTest extends Specification {

    def tested = new DefaultSharedSchemaContextBuilderFactorySupplierResolver()

    @Unroll
    def "should return expected supplier (#expectedSupplier) for file path #filePath and other suppliers #expectedSupplier"()
    {
        given:
            Set<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliersSet = new HashSet<>()
            suppliersSet.addAll(suppliers)
            suppliersSet.add(expectedSupplier)

        when:
            def result = tested.resolveSupplierBasedOnPriorityForFile(filePath, suppliersSet)

        then:
            result.is(expectedSupplier)

        where:
            filePath                    |   suppliers                                                           ||  expectedSupplier
            "somePath/file.yml"         |   [supplier(1, "yml")]                                                ||  supplier(49, "yml")
            "somePath/file.yml"         |   [supplier(1, "yml")]                                                ||  supplier(49, "YML")
            "somePath/file.yml"         |   [supplier(1, "yml")]                                                ||  supplier(49, "yaml", "yml")
            "somePath/file.yml"         |   [supplier(1, "yml"), supplier(50, "yaml")]                          ||  supplier(49, "yml")
            "somePath/someFile.xml"     |   [supplier(32, "json"), supplier(67, "tf"), supplier(12, "xml")]     ||  supplier(37, "xml")
    }

    private IDefaultSharedSchemaContextBuilderFactorySupplier supplier(int priority, String... extensions)
    {
        def result = Mock(IDefaultSharedSchemaContextBuilderFactorySupplier)
        result.getPriority() >> priority
        result.getSupportedFileExtensions() >> asList(extensions)
        result.toString() >> String.format("priority: %d extensions: %s", priority, asList(extensions).toString())
        result
    }
}
