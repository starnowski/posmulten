package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidBasedOnConstantValuesFunctionProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducer
import spock.lang.Specification
import spock.lang.Unroll

class IsTenantValidFunctionInvocationFactoryEnricherTest extends Specification {

    @Unroll
    def "should enrich shared schema context with SQL definition for the function that checks if tenant value is correct based on default values for shares schema context builder for schema #schema and black list values (#blacklist)"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createValidTenantValueConstraint(blacklist, null, null)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            IIsTenantValidBasedOnConstantValuesFunctionProducerParameters capturedParameters = null
            def mockedSQLDefinition = Mock(IsTenantValidBasedOnConstantValuesFunctionDefinition)
            def producer = Mock(IsTenantValidBasedOnConstantValuesFunctionProducer)
            IsTenantValidFunctionInvocationFactoryEnricher tested = new IsTenantValidFunctionInvocationFactoryEnricher(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * producer.produce(_) >>  {
                parameters ->
                    capturedParameters = parameters[0]
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().contains(mockedSQLDefinition)
            result.getIIsTenantValidFunctionInvocationFactory().is(mockedSQLDefinition)

        and: "passed parameters should match default values"
            capturedParameters.getSchema() == schema
            capturedParameters.getBlacklistTenantIds() == new HashSet<String>(blacklist)
            capturedParameters.getArgumentType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
            capturedParameters.getFunctionName() == "is_tenant_identifier_valid"

        where:
            schema          |   blacklist
            null            |   ["ADFZ", "DFZCXVZ"]
            null            |   ["10.22", "9990"]
            "public"        |   ["ADFZ", "DFZCXVZ"]
            "public"        |   ["10.22", "9990"]
            "some_schema"   |   ["BADSF", "DSFZCV"]
            "some_schema"   |   ["10.22", "9990"]
    }

    @Unroll
    def "should enrich shared schema context with SQL definition for the function that checks if tenant value is correct based on custom values for shares schema context builder for schema #schema and black list values (#blacklist) and function name #isTenantValidFunctionName and constraint name #isTenantValidConstraintName"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createValidTenantValueConstraint(blacklist, isTenantValidFunctionName, isTenantValidConstraintName)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            IIsTenantValidBasedOnConstantValuesFunctionProducerParameters capturedParameters = null
            def mockedSQLDefinition = Mock(IsTenantValidBasedOnConstantValuesFunctionDefinition)
            def producer = Mock(IsTenantValidBasedOnConstantValuesFunctionProducer)
            IsTenantValidFunctionInvocationFactoryEnricher tested = new IsTenantValidFunctionInvocationFactoryEnricher(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * producer.produce(_) >>  {
                parameters ->
                    capturedParameters = parameters[0]
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().contains(mockedSQLDefinition)
            result.getIIsTenantValidFunctionInvocationFactory().is(mockedSQLDefinition)

        and: "passed parameters should match default values"
            capturedParameters.getSchema() == schema
            capturedParameters.getBlacklistTenantIds() == new HashSet<String>(blacklist)
            capturedParameters.getArgumentType() == sharedSchemaContextRequest.getCurrentTenantIdPropertyType()
            capturedParameters.getFunctionName() == isTenantValidFunctionName

        where:
            schema          |   blacklist               |   isTenantValidFunctionName   |   isTenantValidConstraintName
            null            |   ["ADFZ", "DFZCXVZ"]     |   "is_t_val"                  |   null
            null            |   ["10.22", "9990"]       |   "is_t_val"                  |   "ten_const_va"
            "public"        |   ["ADFZ", "DFZCXVZ"]     |   "tenant_valid"              |   "ten_const_va"
            "public"        |   ["10.22", "9990"]       |   "tenant_valid"              |   null
            "some_schema"   |   ["BADSF", "DSFZCV"]     |   "tenant_valid"              |   null
            "some_schema"   |   ["10.22", "9990"]       |   "is_t_valid"                |   "ten_const_va"
    }
}
