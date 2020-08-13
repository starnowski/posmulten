package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import spock.lang.Specification

class TenantHasAuthoritiesFunctionDefinitionEnricherTest extends Specification {

    def tested = new TenantHasAuthoritiesFunctionDefinitionEnricher()

    def "should enrich shared schema context with sql definition for function that passed tenant id is equal to current tenant id based on default values for shares schema context builder"()
    {
        given:
            def sharedSchemaContextRequest = new DefaultSharedSchemaContextBuilder().getSharedSchemaContextRequest()
            def context = new SharedSchemaContext()
            def capturedEqualsCurrentTenantIdentifierFunctionProducerParameters = null
            def capturedTenantHasAuthoritiesFunctionProducerParameters = null
            def getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            def mockedEqualsCurrentTenantIdentifierFunctionDefinition = Mock(EqualsCurrentTenantIdentifierFunctionDefinition)
            def mockedTenantHasAuthoritiesFunctionDefinition = Mock(TenantHasAuthoritiesFunctionDefinition)
            def equalsCurrentTenantIdentifierFunctionProducer = Mock(EqualsCurrentTenantIdentifierFunctionProducer)
            def tenantHasAuthoritiesFunctionProducer = Mock(TenantHasAuthoritiesFunctionProducer)
            tested.setEqualsCurrentTenantIdentifierFunctionProducer(equalsCurrentTenantIdentifierFunctionProducer)
            tested.setTenantHasAuthoritiesFunctionProducer(tenantHasAuthoritiesFunctionProducer)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)

        when:
        def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * equalsCurrentTenantIdentifierFunctionProducer.produce(_) >>  {
                parameters ->
                    capturedEqualsCurrentTenantIdentifierFunctionProducerParameters = parameters[0]
                    mockedEqualsCurrentTenantIdentifierFunctionDefinition
            }
            result.getSqlDefinitions().contains(mockedEqualsCurrentTenantIdentifierFunctionDefinition)

        then:
            1 * tenantHasAuthoritiesFunctionProducer.produce(_) >>  {
                parameters ->
                    capturedTenantHasAuthoritiesFunctionProducerParameters = parameters[0]
                    mockedTenantHasAuthoritiesFunctionDefinition
            }
            result.getSqlDefinitions().contains(mockedTenantHasAuthoritiesFunctionDefinition)
            result.getTenantHasAuthoritiesFunctionInvocationFactory().is(mockedTenantHasAuthoritiesFunctionDefinition)

        and: "passed parameters should match default values"
            capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getSchema() == sharedSchemaContextRequest.getDefaultSchema()
            capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getArgumentType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
            capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getCurrentTenantIdFunctionInvocationFactory() == getCurrentTenantIdFunctionInvocationFactory
            capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getFunctionName() == "set_current_tenant_id"
    }

//    @Unroll
//    def "should enrich shared schema context with sql definition for function that passed tenant id is equal to current tenant id based on defined values for builder, defaultSchema: #defaultSchema, currentTenantIdProperty #urrentTenantIdProperty, currentTenantIdPropertyType #currentTenantIdPropertyType, function name: #functionName"()
//    {
//        given:
//        def builder = new DefaultSharedSchemaContextBuilder()
//        builder.setDefaultSchema(defaultSchema)
//        builder.setCurrentTenantIdProperty(currentTenantIdProperty)
//        builder.setCurrentTenantIdPropertyType(currentTenantIdPropertyType)
//        builder.setSetCurrentTenantIdFunctionName(functionName)
//        def sharedSchemaContextRequest = builder.getSharedSchemaContextRequest()
//        def context = new SharedSchemaContext()
//        def capturedParameters = null
//        def mockedSQLDefinition = Mock(SetCurrentTenantIdFunctionDefinition)
//        def equalsCurrentTenantIdentifierFunctionProducer = Mock(EqualsCurrentTenantIdentifierFunctionProducer)
//        def tenantHasAuthoritiesFunctionProducer = Mock(TenantHasAuthoritiesFunctionProducer)
//        tested.setEqualsCurrentTenantIdentifierFunctionProducer(equalsCurrentTenantIdentifierFunctionProducer)
//        tested.setTenantHasAuthoritiesFunctionProducer(tenantHasAuthoritiesFunctionProducer)
//
//        when:
//        def result = tested.enrich(context, sharedSchemaContextRequest)
//
//        then:
//        1 * producer.produce(_) >>  {
//            parameters ->
//                capturedParameters = parameters[0]
//                mockedSQLDefinition
//        }
//        result.getSqlDefinitions().contains(mockedSQLDefinition)
//        result.getISetCurrentTenantIdFunctionInvocationFactory().is(mockedSQLDefinition)
//
//        and: "passed parameters should match default values"
//        capturedParameters.getSchema() == sharedSchemaContextRequest.getDefaultSchema()
//        capturedParameters.getCurrentTenantIdProperty() == sharedSchemaContextRequest.getCurrentTenantIdProperty()
//        capturedParameters.getArgumentType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
//        capturedParameters.getFunctionName() == functionName
//
//        where:
//        defaultSchema   |   currentTenantIdProperty |   currentTenantIdPropertyType |   functionName
//        null            |   "c.tenant_id"           |   "text"                      |   "what_is_tenant_id"
//        "public"        |   "c.tenant_id"           |   "text"                      |   "what_is_tenant_id"
//        "some_sche1"    |   "posmulte.prop.tenant"  |   "Some_SQL_TYPE"             |   "get_tenant_id"
//    }
}
