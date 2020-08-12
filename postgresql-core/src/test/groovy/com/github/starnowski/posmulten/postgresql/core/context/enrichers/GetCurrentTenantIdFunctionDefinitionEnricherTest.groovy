package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionProducer
import spock.lang.Specification

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
}
