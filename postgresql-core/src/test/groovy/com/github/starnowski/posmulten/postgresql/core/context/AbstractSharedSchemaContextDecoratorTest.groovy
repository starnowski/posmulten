package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory
import spock.lang.Specification

abstract class AbstractSharedSchemaContextDecoratorTest<T extends AbstractSharedSchemaContextDecorator> extends Specification {
    def "GetSqlDefinitions"() {
    }

    def "GetTenantHasAuthoritiesFunctionInvocationFactory"() {
    }

    def "GetIGetCurrentTenantIdFunctionInvocationFactory"() {
    }

    def "GetISetCurrentTenantIdFunctionInvocationFactory"() {
        given:
            def tenantId = "testTenant"
            def val1 = "ccx"
            def val2 = "hfdgfvxvb"
            def testStatement = "SELECT fun( " + getFirstTemplateVariable() + "and second part " + getSecondTemplateVariable() + "end"
            def expectedStatement = "SELECT fun( " + val1 + "and second part " + val2 + "end"
            ISharedSchemaContext sharedSchemaContext = Mock(ISharedSchemaContext)
            def tested = prepareTestedObject(sharedSchemaContext, val1, val2)
            ISetCurrentTenantIdFunctionInvocationFactory factory = Mock(ISetCurrentTenantIdFunctionInvocationFactory)

        when:
            def result = tested.getISetCurrentTenantIdFunctionInvocationFactory().generateStatementThatSetTenant(tenantId)

        then:
            1 * sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory() >> factory
            1 * factory.generateStatementThatSetTenant(tenantId) >> testStatement
            result == expectedStatement
    }

    def "GetISetCurrentTenantIdFunctionPreparedStatementInvocationFactory"() {
        given:
            def val1 = "ggga"
            def val2 = "zcv2333"
            def testStatement = "some statement " + getFirstTemplateVariable() + "and second part " + getSecondTemplateVariable() + "end"
            def expectedStatement = "some statement " + val1 + "and second part " + val2 + "end"
            ISharedSchemaContext sharedSchemaContext = Mock(ISharedSchemaContext)
            def tested = prepareTestedObject(sharedSchemaContext, val1, val2)
            ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory factory = Mock(ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory)

        when:
            def result = tested.getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory().returnPreparedStatementThatSetCurrentTenant()

        then:
            1 * sharedSchemaContext.getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory() >> factory
            1 * factory.returnPreparedStatementThatSetCurrentTenant() >> testStatement
            result == expectedStatement
    }


    def "GetTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap"() {
    }

    def "GetIIsTenantValidFunctionInvocationFactory"() {
        given:
            def val1 = "hgg"
            def val2 = "zcvsfgsdfg2333"
            def testStatement = "some statement " + getFirstTemplateVariable() + " FROM " + getSecondTemplateVariable() + "to then end"
            def expectedStatement = "some statement " + val1 + " FROM " + val2 + "to then end"
            ISharedSchemaContext sharedSchemaContext = Mock(ISharedSchemaContext)
            def tested = prepareTestedObject(sharedSchemaContext, val1, val2)
            IIsTenantValidFunctionInvocationFactory factory = Mock(IIsTenantValidFunctionInvocationFactory)
            FunctionArgumentValue functionArgumentValue = Mock(FunctionArgumentValue)

        when:
            def result = tested.getIIsTenantValidFunctionInvocationFactory().returnIsTenantValidFunctionInvocation(functionArgumentValue)

        then:
            1 * sharedSchemaContext.getIIsTenantValidFunctionInvocationFactory() >> factory
            1 * factory.returnIsTenantValidFunctionInvocation(functionArgumentValue) >> testStatement
            result == expectedStatement
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
            1 * sharedSchemaContext.getCurrentTenantIdPropertyType() >> testStatement
            result == expectedStatement
    }

    abstract String getFirstTemplateVariable()
    abstract String getSecondTemplateVariable()
    abstract T prepareTestedObject(ISharedSchemaContext context, String value1, String value2)
}
