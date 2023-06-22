package com.github.starnowski.posmulten.postgresql.core.context

import spock.lang.Specification

abstract class AbstractSharedSchemaContextDecoratorTest<T extends AbstractSharedSchemaContextDecorator> extends Specification {
    def "GetSqlDefinitions"() {
    }

    def "GetTenantHasAuthoritiesFunctionInvocationFactory"() {
    }

    def "GetIGetCurrentTenantIdFunctionInvocationFactory"() {
    }

    def "GetISetCurrentTenantIdFunctionInvocationFactory"() {
    }

    def "GetISetCurrentTenantIdFunctionPreparedStatementInvocationFactory"() {
    }

    def "GetTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap"() {
    }

    def "GetIIsTenantValidFunctionInvocationFactory"() {
    }

    def "GetCurrentTenantIdPropertyType"() {
        given:
            def val1 = "Value1"
            def val2 = "XXYZ"
            def testStatement = "replaces_first_" + getFirstTemplateVariable() + "and second part " + getSecondTemplateVariable() + "end"
            def expectedStatement = "replaces_first_" + val1 + "and second part " + val2 + "end"
            ISharedSchemaContext sharedSchemaContext = Mock(ISharedSchemaContext)
            def tested = prepareTestedObject(sharedSchemaContext, val1, val2)

        when:
            def result = tested.getCurrentTenantIdPropertyType()

        then:
            sharedSchemaContext.getCurrentTenantIdPropertyType() >> testStatement
            result == expectedStatement
    }

    abstract String getFirstTemplateVariable()
    abstract String getSecondTemplateVariable()
    abstract T prepareTestedObject(ISharedSchemaContext context, String value1, String value2)
}
