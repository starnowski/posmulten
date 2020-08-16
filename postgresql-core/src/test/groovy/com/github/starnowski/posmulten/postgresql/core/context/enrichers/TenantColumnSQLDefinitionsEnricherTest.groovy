package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.CreateColumnStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetNotNullStatementProducer
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import spock.lang.Specification

class TenantColumnSQLDefinitionsEnricherTest extends Specification {

    def tested = new TenantColumnSQLDefinitionsEnricher()

    def "should enrich shared schema context with sql definition for function that passed tenant id is equal to current tenant id based on default values for shares schema context builder"()
    {
        given:
        def sharedSchemaContextRequest = new DefaultSharedSchemaContextBuilder().getSharedSchemaContextRequest()
        def context = new SharedSchemaContext()
        def capturedICreateColumnStatementProducerParameters = null
        def capturedISetDefaultStatementProducerParameters = null
        def capturedISetNotNullStatementProducerParameters = null
        def getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
        def createColumnStatementProducerSQLDefinition = Mock(SQLDefinition)
        def setDefaultStatementProducerSQLDefinition = Mock(SQLDefinition)
        def setNotNullStatementProducerSQLDefinition = Mock(SQLDefinition)
        def createColumnStatementProducer = Mock(CreateColumnStatementProducer)
        def setDefaultStatementProducer = Mock(SetDefaultStatementProducer)
        def setNotNullStatementProducer = Mock(SetNotNullStatementProducer)
        tested.setCreateColumnStatementProducer(createColumnStatementProducer)
        tested.setSetDefaultStatementProducer(setDefaultStatementProducer)
        tested.setSetNotNullStatementProducer(setNotNullStatementProducer)
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

        and: "generated sql definitions should be added in correct order"
        result.getSqlDefinitions() == [mockedEqualsCurrentTenantIdentifierFunctionDefinition, mockedTenantHasAuthoritiesFunctionDefinition]

        and: "passed parameters should match default values"
        capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getSchema() == sharedSchemaContextRequest.getDefaultSchema()
        capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getArgumentType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
        capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getCurrentTenantIdFunctionInvocationFactory() == getCurrentTenantIdFunctionInvocationFactory
        capturedEqualsCurrentTenantIdentifierFunctionProducerParameters.getFunctionName() == "is_id_equals_current_tenant_id"

        capturedTenantHasAuthoritiesFunctionProducerParameters.getSchema() == sharedSchemaContextRequest.getDefaultSchema()
        capturedTenantHasAuthoritiesFunctionProducerParameters.getFunctionName() == "tenant_has_authorities"
        capturedTenantHasAuthoritiesFunctionProducerParameters.getEqualsCurrentTenantIdentifierFunctionInvocationFactory() == mockedEqualsCurrentTenantIdentifierFunctionDefinition
        capturedTenantHasAuthoritiesFunctionProducerParameters.getTenantIdArgumentType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
        capturedTenantHasAuthoritiesFunctionProducerParameters.getPermissionCommandPolicyArgumentType() == null
        capturedTenantHasAuthoritiesFunctionProducerParameters.getRLSExpressionArgumentType() == null
        capturedTenantHasAuthoritiesFunctionProducerParameters.getTableArgumentType() == null
        capturedTenantHasAuthoritiesFunctionProducerParameters.getSchemaArgumentType() == null
    }
}
