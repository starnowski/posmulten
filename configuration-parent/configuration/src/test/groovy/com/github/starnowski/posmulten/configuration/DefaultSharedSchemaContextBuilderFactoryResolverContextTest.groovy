package com.github.starnowski.posmulten.configuration

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Supplier

import static java.util.stream.Collectors.toList

class DefaultSharedSchemaContextBuilderFactoryResolverContextTest extends Specification {

    def "should create context object with default components"()
    {
        when:
            def result = DefaultSharedSchemaContextBuilderFactoryResolverContext.builder().build()

        then:
            result
            DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher.class == result.getDefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher().getClass()
            DefaultSharedSchemaContextBuilderFactorySupplierResolver.class == result.getDefaultSharedSchemaContextBuilderFactorySupplierResolver().getClass()
    }

    @Unroll
    def "should create context object with passed suppliers : #suppliers"()
    {
        given:
            def tested = DefaultSharedSchemaContextBuilderFactoryResolverContext.builder()
            suppliers.forEach({it ->
                tested.registerSupplier(it.factorySupplier, it.priority, it.extensions.toArray(new String[0]))
            })

        when:
            def result = tested.build()

        then:
            result
            result.getSuppliers()
            result.getSuppliers().stream().map({it -> testObject(it.factorySupplier, it.priority, it.supportedFileExtensions.toArray(new String[0]))}).collect(toList()) == suppliers

        where:
            suppliers << [[testObject({it -> Mock(IDefaultSharedSchemaContextBuilderFactory)}, 45, ["sddfs", "yml"])],
                          [testObject({it -> Mock(IDefaultSharedSchemaContextBuilderFactory)}, 123, ["xml"]), testObject({it -> Mock(IDefaultSharedSchemaContextBuilderFactory)}, 54, ["json"])],
                          [testObject({it -> Mock(IDefaultSharedSchemaContextBuilderFactory)}, 90, ["json", "yaml"]), testObject({it -> Mock(IDefaultSharedSchemaContextBuilderFactory)}, 13, ["json"])]
            ]
    }

    private static DefaultSharedSchemaContextBuilderFactoryResolverContextTestObject testObject(Supplier<IDefaultSharedSchemaContextBuilderFactory> factorySupplier, int priority, List<String> extensions)
    {
        new DefaultSharedSchemaContextBuilderFactoryResolverContextTestObject(factorySupplier, priority, extensions)
    }

    private static class DefaultSharedSchemaContextBuilderFactoryResolverContextTestObject
    {
        Supplier<IDefaultSharedSchemaContextBuilderFactory> factorySupplier
        int priority
        List<String> extensions

        DefaultSharedSchemaContextBuilderFactoryResolverContextTestObject(Supplier<IDefaultSharedSchemaContextBuilderFactory> factorySupplier, int priority, List<String> extensions) {
            this.factorySupplier = factorySupplier
            this.priority = priority
            this.extensions = extensions
        }

        Supplier<IDefaultSharedSchemaContextBuilderFactory> getFactorySupplier() {
            return factorySupplier
        }

        int getPriority() {
            return priority
        }

        List<String> getExtensions() {
            return extensions
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            DefaultSharedSchemaContextBuilderFactoryResolverContextTestObject that = (DefaultSharedSchemaContextBuilderFactoryResolverContextTestObject) o

            if (priority != that.priority) return false
            if (extensions != that.extensions) return false
            if (factorySupplier != that.factorySupplier) return false

            return true
        }

        int hashCode() {
            int result
            result = (factorySupplier != null ? factorySupplier.hashCode() : 0)
            result = 31 * result + priority
            result = 31 * result + (extensions != null ? extensions.hashCode() : 0)
            return result
        }


        @Override
        String toString() {
            return "test{" +
                    "priority=" + priority +
                    ", extensions=" + extensions +
                    '}';
        }
    }
}
