package com.github.starnowski.posmulten.postgresql.core.context.enrichers


import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.SingleTenantColumnSQLDefinitionsProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory
import spock.lang.Specification

class TenantColumnSQLDefinitionsEnricherTest extends Specification {

    def tested = new TenantColumnSQLDefinitionsEnricher()

    def "should enrich shared schema context with sql definition that creates column for storing record tenant id"()
    {
        given:
        def builder = new DefaultSharedSchemaContextBuilder()
        builder.createRLSPolicyForColumn("users", [:], "tenant", "user_policy")
        builder.createTenantColumnForTable("users")
        builder.createRLSPolicyForColumn("comments", [:], "tenant_id", "comments_policy")
        builder.createTenantColumnForTable("comments")
        builder.createRLSPolicyForColumn("some_table", [:], "tenant_id", "comments_policy")
        def sharedSchemaContextRequest = builder.getSharedSchemaContextRequest()
        def context = new SharedSchemaContext()
        def getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
        def usersTableSQLDefinition1 = Mock(SQLDefinition)
        def usersTableSQLDefinition2 = Mock(SQLDefinition)
        def commentsTableSQLDefinition1 = Mock(SQLDefinition)
        def singleTenantColumnSQLDefinitionsProducer = Mock(SingleTenantColumnSQLDefinitionsProducer)
        tested.setSingleTenantColumnSQLDefinitionsProducer(singleTenantColumnSQLDefinitionsProducer)
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

        and: "generated sql definitions should be added in correct order"
        result.getSqlDefinitions() == [usersTableSQLDefinition1, usersTableSQLDefinition2, commentsTableSQLDefinition1]

    }
}
