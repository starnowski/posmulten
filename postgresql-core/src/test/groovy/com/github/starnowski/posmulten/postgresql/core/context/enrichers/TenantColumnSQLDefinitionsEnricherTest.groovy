package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.*
import com.github.starnowski.posmulten.postgresql.test.utils.RandomString
import spock.lang.Specification

class TenantColumnSQLDefinitionsEnricherTest extends Specification {

    def tested = new TenantColumnSQLDefinitionsEnricher()

    def "should enrich shared schema context with sql definition that creates column for storing record tenant id"()
    {
        given:
            def randomString = new RandomString(5, new Random(), RandomString.lower)
            def builder = new DefaultSharedSchemaContextBuilder()
            builder.createRLSPolicyForColumn("users", [:], "tenant", "user_policy")
            builder.createTenantColumnForTable("users")
            builder.createRLSPolicyForColumn("comments", [:], "tenant_id", "comments_policy")
            builder.createTenantColumnForTable("comments")
            builder.createRLSPolicyForColumn("some_table", [:], "tenant_xxx_id", "some_table_policy")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequest()
            def context = new SharedSchemaContext()
            def currentTenantInvocation = randomString.nextString()
            def getCurrentTenantIdFunctionInvocationFactory = {
                currentTenantInvocation
            }
            def usersTableSQLDefinition1 = Mock(SQLDefinition)
            def usersTableSQLDefinition2 = Mock(SQLDefinition)
            def commentsTableSQLDefinition1 = Mock(SQLDefinition)
            def singleTenantColumnSQLDefinitionsProducer = Mock(SingleTenantColumnSQLDefinitionsProducer)
            tested.setSingleTenantColumnSQLDefinitionsProducer(singleTenantColumnSQLDefinitionsProducer)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
            def usersTableKey = tk("users", null)
            def commentsTableKey = tk("comments", null)
            def someTableKey = tk("some_table", null)

            AbstractTableColumns usersTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(usersTableKey)
            AbstractTableColumns commentsTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)
            AbstractTableColumns someTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * singleTenantColumnSQLDefinitionsProducer.produce(usersTableKey, usersTableColumns, currentTenantInvocation, sharedSchemaContextRequest.getCurrentTenantIdProperty(), sharedSchemaContextRequest.getCurrentTenantIdPropertyType()) >> [usersTableSQLDefinition1, usersTableSQLDefinition2]
            1 * singleTenantColumnSQLDefinitionsProducer.produce(commentsTableKey, commentsTableColumns, currentTenantInvocation, sharedSchemaContextRequest.getCurrentTenantIdProperty(), sharedSchemaContextRequest.getCurrentTenantIdPropertyType()) >> [commentsTableSQLDefinition1]
            0 * singleTenantColumnSQLDefinitionsProducer.produce(someTableKey, someTableColumns, _, _, _)

            result.getSqlDefinitions().contains(usersTableSQLDefinition1)
            result.getSqlDefinitions().contains(usersTableSQLDefinition2)
            result.getSqlDefinitions().contains(commentsTableSQLDefinition1)

        and: "sql definitions are added in order returned by component of type SingleTenantColumnSQLDefinitionsProducer"
            result.getSqlDefinitions().indexOf(usersTableSQLDefinition1) < result.getSqlDefinitions().indexOf(usersTableSQLDefinition2)
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
