package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantFunctionDefinitionProducer
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.MapBuilder.mapBuilder

class IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricherTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher()

    @Unroll
    def "should create all required SQL definition that creates functions that checks if records from specified tables exists for schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForColumn("users", [id: "N/A"], "tenant", "N/A")
            builder.createRLSPolicyForColumn("comments", [uuid: "N/A"], "tenant_id", "N/A")
            builder.createRLSPolicyForColumn("some_table", [somedid: "N/A"], "tenant_xxx_id", "N/A")
            builder.createSameTenantConstraintForForeignKey("comments", "users", mapBuilder().put("N/A", "N/A").build(), "N/A")
            builder.createSameTenantConstraintForForeignKey("some_table", "users", mapBuilder().put("N/A", "N/A").build(), "N/A")
            builder.createSameTenantConstraintForForeignKey("some_table", "comments", mapBuilder().put("N/A", "N/A").build(), "N/A")
            builder.setNameForFunctionThatChecksIfRecordExistsInTable("users", "is_user_exists")
            builder.setNameForFunctionThatChecksIfRecordExistsInTable("comments", "is_comment_exists")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
            def usersTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def commentsTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def isRecordBelongsToCurrentTenantFunctionDefinitionProducer = Mock(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            tested.setIsRecordBelongsToCurrentTenantFunctionDefinitionProducer(isRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def usersTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(usersTableKey)
            def commentsTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(usersTableKey, usersTableColumns, iGetCurrentTenantIdFunctionInvocationFactory, "is_user_exists", schema) >> usersTableSQLDefinition
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(commentsTableKey, commentsTableColumns, iGetCurrentTenantIdFunctionInvocationFactory, "is_comment_exists", schema) >> commentsTableSQLDefinition
            0 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(_)

            result.getSqlDefinitions().contains(usersTableSQLDefinition)
            result.getSqlDefinitions().contains(commentsTableSQLDefinition)

        and: "put function objects into map"
            result.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().get(usersTableKey) == usersTableSQLDefinition
            result.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().get(commentsTableKey) == commentsTableSQLDefinition

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should not create any sql definitions when there is no request for constraint creation in #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForColumn("users", [id: "N/A"], "tenant", "N/A")
            builder.createRLSPolicyForColumn("comments", [uuid: "N/A"], "tenant_id", "N/A")
            builder.createRLSPolicyForColumn("some_table", [somedid: "N/A"], "tenant_xxx_id", "N/A")
            builder.setNameForFunctionThatChecksIfRecordExistsInTable("users", "is_user_exists")
            builder.setNameForFunctionThatChecksIfRecordExistsInTable("comments", "is_comment_exists")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
            def isRecordBelongsToCurrentTenantFunctionDefinitionProducer = Mock(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            tested.setIsRecordBelongsToCurrentTenantFunctionDefinitionProducer(isRecordBelongsToCurrentTenantFunctionDefinitionProducer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            0 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(_)
            result.getSqlDefinitions().isEmpty()

        where:
            schema << [null, "public", "some_schema"]
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
