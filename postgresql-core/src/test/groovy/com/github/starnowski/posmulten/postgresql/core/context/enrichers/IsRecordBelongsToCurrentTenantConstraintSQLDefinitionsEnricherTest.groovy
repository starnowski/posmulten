package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.*
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingConstraintNameDeclarationForTableException
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingIsRecordBelongsToCurrentTenantFunctionInvocationFactoryException
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.MapBuilder.mapBuilder

class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricherTest extends Specification {

    @Unroll
    def "should create all required SQL definitions that create a constraint that checks if the foreign key reference to the record that belongs to the same tenant in schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("users", [:], "tenant", "N/A")
            builder.createRLSPolicyForTable("comments", [:], "tenant_id", "N/A")
            builder.createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "N/A")
            builder.createSameTenantConstraintForForeignKey("comments", "users", mapBuilder().put("user_id", "id").build(), "comments_users_fk_con")
            builder.createSameTenantConstraintForForeignKey("some_table", "users", mapBuilder().put("owner_id", "id").build(), "some_table_same_tenant_users_con")
            builder.createSameTenantConstraintForForeignKey("some_table", "comments", mapBuilder().put("some_comment_id", "uuid").build(), "some_table_comments_const_ten")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def someTableKey = tk("some_table", schema)
            def isUserBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            def isCommentBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(usersTableKey, isUserBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(commentsTableKey, isCommentBelongsToCurrentTenantFunctionInvocationFactory)

            def isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = Mock(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            def tested = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)

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

    @Unroll
    def "should return non sql definition when there is no foreign key constraint request for schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("users", [:], "tenant", "N/A")
            builder.createRLSPolicyForTable("comments", [:], "tenant_id", "N/A")
            builder.createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "N/A")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = Mock(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            def tested = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            0 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(_)
            result.getSqlDefinitions().size() == 0

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should create all required SQL definitions that create a constraint that checks if the foreign key reference to the record that belongs to the same tenant even if there are multiple foreign key columns that store a reference to the same table in schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("users", [:], "tenant", "N/A")
            builder.createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "N/A")
            builder.createSameTenantConstraintForForeignKey("some_table", "users", mapBuilder().put("owner_id", "id").build(), "some_table_same_tenant_users_owner_con")
            builder.createSameTenantConstraintForForeignKey("some_table", "users", mapBuilder().put("parent_id", "id").build(), "some_table_same_tenant_users_parent_con")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def usersTableKey = tk("users", schema)
            def someTableKey = tk("some_table", schema)
            def isUserBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(usersTableKey, isUserBelongsToCurrentTenantFunctionInvocationFactory)

            def isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = Mock(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            def tested = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)

            def expectedSomeTableUserOwnerConstraintParameters = IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder()
                    .withConstraintName("some_table_same_tenant_users_owner_con")
                    .withTableKey(someTableKey)
                    .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isUserBelongsToCurrentTenantFunctionInvocationFactory)
                    .withForeignKeyPrimaryKeyMappings(mapBuilder().put("owner_id", "id").build())
                    .build()

            def expectedSomeTableUserParentConstraintParameters = IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder()
                    .withConstraintName("some_table_same_tenant_users_parent_con")
                    .withTableKey(someTableKey)
                    .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isUserBelongsToCurrentTenantFunctionInvocationFactory)
                    .withForeignKeyPrimaryKeyMappings(mapBuilder().put("parent_id", "id").build())
                    .build()

            def isSomeTableUserOwnerBelongsToSameTenantConstraint = Mock(SQLDefinition)
            def isSomeTableUserParentBelongsToSameTenantConstraint = Mock(SQLDefinition)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(expectedSomeTableUserOwnerConstraintParameters) >> [isSomeTableUserOwnerBelongsToSameTenantConstraint]
            1 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(expectedSomeTableUserParentConstraintParameters) >> [isSomeTableUserParentBelongsToSameTenantConstraint]
            0 * isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(_)
            result.getSqlDefinitions().contains(isSomeTableUserOwnerBelongsToSameTenantConstraint)
            result.getSqlDefinitions().contains(isSomeTableUserParentBelongsToSameTenantConstraint)
            result.getSqlDefinitions().size() == 2

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should throw an exception when there missing the constraint name declaration for table #table and schema #schema and foreign keys #foreignKeyPrimaryKeyColumnsMappings"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("tab1", [:], "tenant", "N/A")
            builder.createRLSPolicyForTable(table, [:], "tenant_xxx_id", "N/A")
            builder.createSameTenantConstraintForForeignKey(table, "tab1", foreignKeyPrimaryKeyColumnsMappings, null)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def usersTableKey = tk("tab1", schema)
            def isRecordBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(usersTableKey, isRecordBelongsToCurrentTenantFunctionInvocationFactory)

            def isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = Mock(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            def tested = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)

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

    @Unroll
    def "should throw an exception when there is missing object of IsRecordBelongsToCurrentTenantFunctionInvocationFactory type for table #table and schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("tab1", [:], "tenant", "N/A")
            builder.createRLSPolicyForTable(table, [:], "tenant_xxx_id", "N/A")
            builder.createSameTenantConstraintForForeignKey("tab1", table, [uuid: "N/A"] , "fun_1")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()

            def isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = Mock(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
            def tested = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)

        when:
            tested.enrich(context, sharedSchemaContextRequest)

        then:
            def ex = thrown(MissingIsRecordBelongsToCurrentTenantFunctionInvocationFactoryException)

        and: "message should match"
            ex.message == expectedMessage

        and: "exception object should have correct table key"
            ex.tableKey == tk(table, schema)

        where:
            table       |   schema              ||  expectedMessage
            "users"     |   null                ||  "Missing object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory for table users and schema null"
            "users"     |   "public"            ||  "Missing object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory for table users and schema public"
            "users"     |   "some_other_schema" ||  "Missing object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory for table users and schema some_other_schema"
            "comments"  |   "some_other_schema" ||  "Missing object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory for table comments and schema some_other_schema"
            "comments"  |   null                ||  "Missing object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory for table comments and schema null"
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
