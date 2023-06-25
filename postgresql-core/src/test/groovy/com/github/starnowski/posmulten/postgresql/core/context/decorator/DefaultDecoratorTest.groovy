package com.github.starnowski.posmulten.postgresql.core.context.decorator

import spock.lang.Specification

class DefaultDecoratorTest extends Specification {

    def "should unwarp value that is not implementation of IDecorator"() {
        given:
            Object ob = new Object()
            DefaultDecorator decorator1 = new DefaultDecorator(ob)
            DefaultDecorator decorator2 = new DefaultDecorator(decorator1)

        when:
            def result = decorator2.unwrap()

        then:
            result.is(ob)
    }
}
