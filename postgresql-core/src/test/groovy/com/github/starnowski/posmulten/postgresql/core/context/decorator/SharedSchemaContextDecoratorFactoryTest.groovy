package com.github.starnowski.posmulten.postgresql.core.context.decorator

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import spock.lang.Specification

class SharedSchemaContextDecoratorFactoryTest extends Specification {

    def "test build method"() {
        given:
            def factory1 = Mock(ISharedSchemaContextDecoratorFactory)
            def factory2 = Mock(ISharedSchemaContextDecoratorFactory)
            def factories = [factory1, factory2]
            def sharedSchemaContext = Mock(ISharedSchemaContext)
            def decoratorContext = Mock(DefaultDecoratorContext)
            def decorator1 = Mock(ISharedSchemaContextDecorator)
            def decorator2 = Mock(ISharedSchemaContextDecorator)
            def tested = new SharedSchemaContextDecoratorFactory(factories)

        when:
            def result = tested.build(sharedSchemaContext, decoratorContext)

        then:
            1 * factory1.build(sharedSchemaContext, decoratorContext) >> decorator1
            1 * factory2.build(decorator1, decoratorContext) >> decorator2
            result == decorator2
    }

    def "should correctly initialized factories array"(){
        given:
            def tested = new SharedSchemaContextDecoratorFactory()

        when:
            def results = tested.getFactories()

        then:
            results.collect { it.getClass() } == [BasicSharedSchemaContextDecoratorFactory]
    }
}
