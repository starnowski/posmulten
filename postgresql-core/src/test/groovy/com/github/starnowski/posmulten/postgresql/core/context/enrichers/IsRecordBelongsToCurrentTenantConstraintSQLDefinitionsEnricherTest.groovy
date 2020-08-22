package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.MapBuilder.mapBuilder

class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricherTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher()

    @Unroll
    def "should create all required SQL definition that creates functions that checks if records from specified tables exists for schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForColumn("users", [id: "N/A"], "tenant", "N/A")
            builder.createRLSPolicyForColumn("comments", [uuid: "N/A"], "tenant_id", "N/A")
            builder.createRLSPolicyForColumn("some_table", [somedid: "N/A"], "tenant_xxx_id", "N/A")
            builder.createSameTenantConstraintForForeignKey("comments", "users", mapBuilder().put("user_id", "id").build(), "N/A")
            builder.createSameTenantConstraintForForeignKey("some_table", "users", mapBuilder().put("owner_id", "id").build(), "N/A")
            builder.createSameTenantConstraintForForeignKey("some_table", "comments", mapBuilder().put("some_comment_id", "uuid").build(), "N/A")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequest()
            def context = new SharedSchemaContext()
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def isUserBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            def isCommentBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(usersTableKey, isUserBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(commentsTableKey, isCommentBelongsToCurrentTenantFunctionInvocationFactory)

            def isCommentsUserBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableUserBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableCommentBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def commentsTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = Mock(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            tested.setIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer(isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            def usersTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(usersTableKey)
            def commentsTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(usersTableKey, usersTableColumns, iGetCurrentTenantIdFunctionInvocationFactory, "is_user_exists", schema) >> usersTableSQLDefinition
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(commentsTableKey, commentsTableColumns, iGetCurrentTenantIdFunctionInvocationFactory, "is_comment_exists", schema) >> commentsTableSQLDefinition
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(commentsTableKey, commentsTableColumns, iGetCurrentTenantIdFunctionInvocationFactory, "is_comment_exists", schema) >> commentsTableSQLDefinition
            0 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(_)

            result.getSqlDefinitions().contains(usersTableSQLDefinition)
            result.getSqlDefinitions().contains(commentsTableSQLDefinition)

        where:
        schema << [null, "public", "some_schema"]
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
