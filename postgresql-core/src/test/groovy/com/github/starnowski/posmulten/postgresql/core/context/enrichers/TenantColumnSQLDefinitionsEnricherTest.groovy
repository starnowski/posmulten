package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.*
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableException
import com.github.starnowski.posmulten.postgresql.test.utils.RandomString
import spock.lang.Specification
import spock.lang.Unroll

import static java.lang.String.format

class TenantColumnSQLDefinitionsEnricherTest extends Specification {

    def tested = new TenantColumnSQLDefinitionsEnricher()

    @Unroll
    def "should enrich shared schema context with sql definition that creates column for storing record tenant id with schema #schema"()
    {
        given:
            def randomString = new RandomString(5, new Random(), RandomString.lower)
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("users", [:], "tenant", "user_policy")
            builder.createTenantColumnForTable("users")
            builder.createRLSPolicyForTable("comments", [:], "tenant_id", "comments_policy")
            builder.createTenantColumnForTable("comments")
            builder.createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "some_table_policy")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
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
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def someTableKey = tk("some_table", schema)

            ITableColumns usersTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(usersTableKey)
            ITableColumns commentsTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)
            ITableColumns someTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)

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

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should not create any sql definitions when there is no request for creation of the tenant column in #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("users", [:], "tenant", "user_policy")
            builder.createRLSPolicyForTable("comments", [:], "tenant_id", "comments_policy")
            builder.createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "some_table_policy")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def singleTenantColumnSQLDefinitionsProducer = Mock(SingleTenantColumnSQLDefinitionsProducer)
            tested.setSingleTenantColumnSQLDefinitionsProducer(singleTenantColumnSQLDefinitionsProducer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            0 * singleTenantColumnSQLDefinitionsProducer.produce(_, _, _, _, _)
            result.getSqlDefinitions().isEmpty()

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should throw an exception when there missing the RLS policy declaration for a table (#table) for which the tenant column should be created with schema #schema"()
    {
        given:
            def randomString = new RandomString(5, new Random(), RandomString.lower)
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createTenantColumnForTable(table)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def currentTenantInvocation = randomString.nextString()
            def getCurrentTenantIdFunctionInvocationFactory = {
                currentTenantInvocation
            }
            def singleTenantColumnSQLDefinitionsProducer = Mock(SingleTenantColumnSQLDefinitionsProducer)
            tested.setSingleTenantColumnSQLDefinitionsProducer(singleTenantColumnSQLDefinitionsProducer)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)

        when:
            tested.enrich(context, sharedSchemaContextRequest)

        then:
            def ex = thrown(MissingRLSPolicyDeclarationForTableException)

        and: "message should match"
            ex.message == format("Missing RLS policy declaration for table %1\$s in schema %2\$s", table, schema)

        and: "exception object should have correct table key"
            ex.tableKey == tk(table, schema)

        where:
            table       |   schema
            "users"     |   null
            "users"     |   "public"
            "users"     |   "some_other_schema"
            "comments"  |   "some_other_schema"
            "comments"  |   null

    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
