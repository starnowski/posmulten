package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.DefaultForeignKeyConstraintStatementParameters
import com.github.starnowski.posmulten.postgresql.core.ForeignKeyConstraintStatementProducer
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingConstraintNameDeclarationForTableException
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
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
                    .withForeignKeyColumnMappings(mapBuilder().put("user_id", "id").put("tenant_id", "tenant").build())
                    .build()

            def expectedSomeTableUserConstraintParameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName("some_table_same_tenant_users_con")
                    .withTableName(someTableKey.getTable())
                    .withTableSchema(someTableKey.getSchema())
                    .withReferenceTableKey(usersTableKey)
                    .withForeignKeyColumnMappings(mapBuilder().put("owner_id", "id").put("tenant_xxx_id", "tenant").build())
                    .build()

            def expectedSomeTableCommentConstraintParameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName("some_table_comments_const_ten")
                    .withTableName(someTableKey.getTable())
                    .withTableSchema(someTableKey.getSchema())
                    .withReferenceTableKey(commentsTableKey)
                    .withForeignKeyColumnMappings(mapBuilder().put("some_comment_id", "uuid").put("tenant_xxx_id", "tenant_id").build())
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
                    .setCreateForeignKeyConstraintWithTenantColumn(true)
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

    @Unroll
    def "should return non sql definition when the createForeignKeyConstraintWithTenantColumn is null or false (#flag)"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(null)
                    .createRLSPolicyForTable("users", [:], "tenant", "N/A")
                    .createRLSPolicyForTable("comments", [:], "tenant_id", "N/A")
                    .createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "N/A")
                    .setCreateForeignKeyConstraintWithTenantColumn(flag)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def foreignKeyConstraintStatementProducer = Mock(ForeignKeyConstraintStatementProducer)
            def tested = new ForeignKeyConstraintSQLDefinitionsEnricher(foreignKeyConstraintStatementProducer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            result.getSqlDefinitions().size() == 0

        and: "no SQLDefinition producer should be called"
            0 * foreignKeyConstraintStatementProducer.produce(_)

        where:
            flag << [null, false]
    }

    @Unroll
    def "should throw an exception when there missing the constraint name declaration for table #table and schema #schema and foreign keys #foreignKeyPrimaryKeyColumnsMappings"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
                    .createRLSPolicyForTable("tab1", [:], "tenant", "N/A")
                    .createRLSPolicyForTable(table, [:], "tenant_xxx_id", "N/A")
                    .createSameTenantConstraintForForeignKey(table, "tab1", foreignKeyPrimaryKeyColumnsMappings, null)
                    .setCreateForeignKeyConstraintWithTenantColumn(true)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()

            def foreignKeyConstraintStatementProducer = Mock(ForeignKeyConstraintStatementProducer)
            def tested = new ForeignKeyConstraintSQLDefinitionsEnricher(foreignKeyConstraintStatementProducer)

        when:
            tested.enrich(context, sharedSchemaContextRequest)

        then:
            def ex = thrown(MissingConstraintNameDeclarationForTableException)

        and: "message should match"
            ex.message == expectedMessage

        and: "exception object should have correct table key"
            ex.tableKey == tk(table, schema)

        and: "exception object should have correct foreign keys set"
            ex.foreignKeysColumns == foreignKeyPrimaryKeyColumnsMappings.keySet()

        where:
            table       |   schema              |   foreignKeyPrimaryKeyColumnsMappings                     ||  expectedMessage
            "users"     |   null                |   [uuid: "N/A"]                                           ||  "Missing constraint name that in table users and schema null checks  if the foreign key columns (uuid) refers to records that belong to the same tenant"
            "users"     |   "public"            |   [uuid: "N/A"]                                           ||  "Missing constraint name that in table users and schema public checks  if the foreign key columns (uuid) refers to records that belong to the same tenant"
            "users"     |   "some_other_schema" |   [uuid: "N/A"]                                           ||  "Missing constraint name that in table users and schema some_other_schema checks  if the foreign key columns (uuid) refers to records that belong to the same tenant"
            "comments"  |   "some_other_schema" |   [comment_id: "N/A", comment_owner_id: "N/A"]            ||  "Missing constraint name that in table comments and schema some_other_schema checks  if the foreign key columns (comment_id, comment_owner_id) refers to records that belong to the same tenant"
            "comments"  |   null                |   [comment_id: "N/A", comment_owner_id: "N/A"]            ||  "Missing constraint name that in table comments and schema null checks  if the foreign key columns (comment_id, comment_owner_id) refers to records that belong to the same tenant"
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
