package com.github.starnowski.posmulten.postgresql.core.context.decorator

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import spock.lang.Specification
import spock.lang.Unroll

class BasicSharedSchemaContextDecoratorFactoryTest extends Specification {

    @Unroll
    def "should build correct decorator with #variableValueMap"(){
        given:
            def tested = new BasicSharedSchemaContextDecoratorFactory()
            ISharedSchemaContext sharedSchemaContext = Mock(ISharedSchemaContext)
            DefaultDecoratorContext decoratorContext = Mock(DefaultDecoratorContext)

        when:
            def result = tested.build(sharedSchemaContext, decoratorContext)

        then:
            1 * decoratorContext.getReplaceCharactersMap() >> variableValueMap
            result.getVariableValueMap() == variableValueMap

        and: "unwrap method should return passed context"
            result.unwrap() == sharedSchemaContext

        where:
            variableValueMap << [["par": "azxcvzxv", "it": "Value"], ["some1": "va1"],[:]]
    }
}
