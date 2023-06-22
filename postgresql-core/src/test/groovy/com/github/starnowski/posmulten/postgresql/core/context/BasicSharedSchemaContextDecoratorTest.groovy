package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder

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
}
