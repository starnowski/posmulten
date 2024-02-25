package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.DefaultForeignKeyConstraintStatementParameters
import com.github.starnowski.posmulten.postgresql.core.ForeignKeyConstraintStatementProducer
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

class ForeignKeyConstraintSQLDefinitionsEnricherTest extends Specification {

    @Unroll
    def "should create all required SQL definitions that create a foreign key constraint in schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
                    .createRLSPolicyForTable("users", [:], "tenant", "N/A")
                    .createRLSPolicyForTable("comments", [:], "tenant_id", "N/A")
                    .createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "N/A")
                    .createSameTenantConstraintForForeignKey("comments", "users", mapBuilder().put("user_id", "id").build(), "comments_users_fk_con")
                    .createSameTenantConstraintForForeignKey("some_table", "users", mapBuilder().put("owner_id", "id").build(), "some_table_same_tenant_users_con")
                    .createSameTenantConstraintForForeignKey("some_table", "comments", mapBuilder().put("some_comment_id", "uuid").build(), "some_table_comments_const_ten")
                    .setCreateForeignKeyConstraintWithTenantColumn(true)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def someTableKey = tk("some_table", schema)

            def foreignKeyConstraintStatementProducer = Mock(ForeignKeyConstraintStatementProducer)
            def tested = new ForeignKeyConstraintSQLDefinitionsEnricher(foreignKeyConstraintStatementProducer)

            def expectedCommentUserConstraintParameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName("comments_users_fk_con")
                    .withTableName(commentsTableKey.getTable())
                    .withTableSchema(commentsTableKey.getSchema())
                    .withReferenceTableKey(usersTableKey)
                    .withForeignKeyColumnMappings(mapBuilder().put("user_id", "id").build())
                    .build()

            def expectedSomeTableUserConstraintParameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName("some_table_same_tenant_users_con")
                    .withTableName(someTableKey.getTable())
                    .withTableSchema(someTableKey.getSchema())
                    .withReferenceTableKey(usersTableKey)
                    .withForeignKeyColumnMappings(mapBuilder().put("owner_id", "id").build())
                    .build()

            def expectedSomeTableCommentConstraintParameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName("some_table_comments_const_ten")
                    .withTableName(someTableKey.getTable())
                    .withTableSchema(someTableKey.getSchema())
                    .withReferenceTableKey(commentsTableKey)
                    .withForeignKeyColumnMappings(mapBuilder().put("some_comment_id", "uuid").build())
                    .build()

            def isCommentsUserBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableUserBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableCommentBelongsToSameTenantConstraint = Mock(SQLDefinition)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * foreignKeyConstraintStatementProducer.produce(expectedCommentUserConstraintParameters) >> isCommentsUserBelongsToSameTenantConstraint
            1 * foreignKeyConstraintStatementProducer.produce(expectedSomeTableUserConstraintParameters) >> isSomeTableUserBelongsToSameTenantConstraint
            1 * foreignKeyConstraintStatementProducer.produce(expectedSomeTableCommentConstraintParameters) >> isSomeTableCommentBelongsToSameTenantConstraint
            0 * foreignKeyConstraintStatementProducer.produce(_)
            result.getSqlDefinitions().contains(isSomeTableUserBelongsToSameTenantConstraint)
            result.getSqlDefinitions().contains(isSomeTableCommentBelongsToSameTenantConstraint)
            result.getSqlDefinitions().contains(isCommentsUserBelongsToSameTenantConstraint)
            result.getSqlDefinitions().size() == 3

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should return non sql definition when there is no foreign key constraint request for schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
                    .createRLSPolicyForTable("users", [:], "tenant", "N/A")
                    .createRLSPolicyForTable("comments", [:], "tenant_id", "N/A")
                    .createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "N/A")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def foreignKeyConstraintStatementProducer = Mock(ForeignKeyConstraintStatementProducer)
            def tested = new ForeignKeyConstraintSQLDefinitionsEnricher(foreignKeyConstraintStatementProducer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            0 * foreignKeyConstraintStatementProducer.produce(_)
            result.getSqlDefinitions().size() == 0

        where:
            schema << [null, "public", "some_schema"]
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
