package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.decorator.BasicSharedSchemaContextDecorator
import com.github.starnowski.posmulten.postgresql.core.context.decorator.BasicSharedSchemaContextDecoratorContext
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder
import spock.lang.Unroll

class BasicSharedSchemaContextDecoratorTest extends AbstractSharedSchemaContextDecoratorTest<BasicSharedSchemaContextDecorator> {
    @Override
    String getFirstTemplateVariable() {
        "{{testValue1}}"
    }

    @Override
    String getSecondTemplateVariable() {
        "someTemplateVar}}test"
    }

    @Override
    BasicSharedSchemaContextDecorator prepareTestedObject(ISharedSchemaContext context, String value1, String value2) {
        new BasicSharedSchemaContextDecorator(context, new BasicSharedSchemaContextDecoratorContext() {
            @Override
            Map<String, String> getVariableValueMap() {
                MapBuilder.mapBuilder().put("{{testValue1}}", value1).put("someTemplateVar}}test", value2).build()
            }
        })
    }

    @Unroll
    def "should covert statement '#statement' to expected statement: '#expected', parameters mapping #variableValueMap"(){
        given:
            ISharedSchemaContext context = Mock(ISharedSchemaContext)
            def map = variableValueMap
            def tested = new BasicSharedSchemaContextDecorator(context, new BasicSharedSchemaContextDecoratorContext() {
                @Override
                Map<String, String> getVariableValueMap() {
                    map
                }
            })

        when:
            def result = tested.convert(statement)

        then:
            result == expected

        where:
            statement                               |   variableValueMap                        || expected
            "SELECT"                                |   [:]                                     || "SELECT"
            "SELECT"                                |   ["some1": "va1"]                        || "SELECT"
            "SELECT some1 from fun()"               |   ["some1": "va1"]                        || "SELECT va1 from fun()"
            "UPDATE tHOse it some1 from par()"      |   ["par": "azxcvzxv", "it": "Value"]      || "UPDATE tHOse Value some1 from azxcvzxv()"

    }
}
