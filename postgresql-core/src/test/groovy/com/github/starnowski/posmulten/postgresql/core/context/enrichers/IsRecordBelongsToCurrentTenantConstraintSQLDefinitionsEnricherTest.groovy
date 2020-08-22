package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.*
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
            builder.createRLSPolicyForColumn("users", [:], "tenant", "N/A")
            builder.createRLSPolicyForColumn("comments", [:], "tenant_id", "N/A")
            builder.createRLSPolicyForColumn("some_table", [:], "tenant_xxx_id", "N/A")
            builder.createSameTenantConstraintForForeignKey("comments", "users", mapBuilder().put("user_id", "id").build(), "comments_users_fk_con")
            builder.createSameTenantConstraintForForeignKey("some_table", "users", mapBuilder().put("owner_id", "id").build(), "some_table_same_tenant_users_con")
            builder.createSameTenantConstraintForForeignKey("some_table", "comments", mapBuilder().put("some_comment_id", "uuid").build(), "some_table_comments_const_ten")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequest()
            def context = new SharedSchemaContext()
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def someTableKey = tk("some_table", schema)
            def isUserBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            def isCommentBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(usersTableKey, isUserBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(commentsTableKey, isCommentBelongsToCurrentTenantFunctionInvocationFactory)

            def isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = Mock(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            tested.setIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer(isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)

            def expectedCommentUserConstraintParameters = IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder()
                .withConstraintName("comments_users_fk_con")
                .withTableKey(commentsTableKey)
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isUserBelongsToCurrentTenantFunctionInvocationFactory)
                .withForeignKeyPrimaryKeyMappings(mapBuilder().put("user_id", "id").build())
                .build()

            def expectedSomeTableUserConstraintParameters = IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder()
                .withConstraintName("some_table_same_tenant_users_con")
                .withTableKey(someTableKey)
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isUserBelongsToCurrentTenantFunctionInvocationFactory)
                .withForeignKeyPrimaryKeyMappings(mapBuilder().put("owner_id", "id").build())
                .build()

            def expectedSomeTableCommentConstraintParameters = IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder()
                    .withConstraintName("some_table_comments_const_ten")
                    .withTableKey(someTableKey)
                    .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isCommentBelongsToCurrentTenantFunctionInvocationFactory)
                    .withForeignKeyPrimaryKeyMappings(mapBuilder().put("some_comment_id", "uuid").build())
                    .build()

            def isCommentsUserBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableUserBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableCommentBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableCommentBelongsToSameTenantConstraint1 = Mock(SQLDefinition)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(expectedCommentUserConstraintParameters) >> [isCommentsUserBelongsToSameTenantConstraint]
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(expectedSomeTableUserConstraintParameters) >> [isSomeTableUserBelongsToSameTenantConstraint]
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(expectedSomeTableCommentConstraintParameters) >> [isSomeTableCommentBelongsToSameTenantConstraint, isSomeTableCommentBelongsToSameTenantConstraint1]
            0 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(_)
            result.getSqlDefinitions().contains(isSomeTableUserBelongsToSameTenantConstraint)
            result.getSqlDefinitions().contains(isSomeTableCommentBelongsToSameTenantConstraint)
            result.getSqlDefinitions().contains(isCommentsUserBelongsToSameTenantConstraint)
            result.getSqlDefinitions().size() == 4

        where:
        schema << [null, "public", "some_schema"]
    }

    //TODO No definition produced

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
