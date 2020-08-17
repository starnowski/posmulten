package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionProducer
import spock.lang.Specification
import spock.lang.Unroll

class GetCurrentTenantIdFunctionDefinitionEnricherTest extends Specification {

    def tested = new GetCurrentTenantIdFunctionDefinitionEnricher()

    def "should enrich shared schema context with sql definition for function that returns current tenant id based on default values for shares schema context builder"()
    {
        given:
            def sharedSchemaContextRequest = new DefaultSharedSchemaContextBuilder().getSharedSchemaContextRequest()
            def context = new SharedSchemaContext()
            def capturedParameters = null
            def mockedSQLDefinition = Mock(GetCurrentTenantIdFunctionDefinition)
            def producer = Mock(GetCurrentTenantIdFunctionProducer)
            tested.setGetCurrentTenantIdFunctionProducer(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * producer.produce(_) >>  {
                parameters ->
                    capturedParameters = parameters[0]
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().contains(mockedSQLDefinition)
            result.getIGetCurrentTenantIdFunctionInvocationFactory().is(mockedSQLDefinition)

        and: "passed parameters should match default values"
            capturedParameters.getSchema() == sharedSchemaContextRequest.getDefaultSchema()
            capturedParameters.getCurrentTenantIdProperty() == sharedSchemaContextRequest.getCurrentTenantIdProperty()
            capturedParameters.getFunctionReturnType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
            capturedParameters.getFunctionName() == "get_current_tenant_id"
    }

    @Unroll
    def "should enrich shared schema context with sql definition for function that returns current tenant id based on defined values for builder, defaultSchema: #defaultSchema, currentTenantIdProperty #urrentTenantIdProperty, currentTenantIdPropertyType #currentTenantIdPropertyType, function name: #functionName"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(defaultSchema)
            builder.setCurrentTenantIdProperty(currentTenantIdProperty)
            builder.setCurrentTenantIdPropertyType(currentTenantIdPropertyType)
            builder.setGetCurrentTenantIdFunctionName(functionName)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequest()
            def context = new SharedSchemaContext()
            def capturedParameters = null
            def mockedSQLDefinition = Mock(GetCurrentTenantIdFunctionDefinition)
            def producer = Mock(GetCurrentTenantIdFunctionProducer)
            tested.setGetCurrentTenantIdFunctionProducer(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * producer.produce(_) >>  {
                parameters ->
                    capturedParameters = parameters[0]
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().contains(mockedSQLDefinition)
            result.getIGetCurrentTenantIdFunctionInvocationFactory().is(mockedSQLDefinition)

        and: "passed parameters should match default values"
            capturedParameters.getSchema() == defaultSchema
            capturedParameters.getCurrentTenantIdProperty() == sharedSchemaContextRequest.getCurrentTenantIdProperty()
            capturedParameters.getFunctionReturnType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
            capturedParameters.getFunctionName() == functionName

        where:
            defaultSchema   |   currentTenantIdProperty |   currentTenantIdPropertyType |   functionName
            null            |   "c.tenant_id"           |   "text"                      |   "what_is_tenant_id"
            "public"        |   "c.tenant_id"           |   "text"                      |   "what_is_tenant_id"
            "some_sche1"    |   "posmulte.prop.tenant"  |   "Some_SQL_TYPE"             |   "get_tenant_id"
    }
}
