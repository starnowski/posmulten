package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantFunctionDefinitionProducer
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingFunctionNameDeclarationForTableException
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder
import static com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest.DEFAULT_TENANT_ID_COLUMN
import static java.lang.String.format

class IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricherTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher()

    @Unroll
    def "should create all required SQL definition that creates functions that checks if records from specified tables exists for schema #schema"()
    {
        given:
            def builder = prepareBuilder(schema)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
            def usersTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def commentsTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def isRecordBelongsToCurrentTenantFunctionDefinitionProducer = Mock(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(isRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def usersTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(usersTableKey)
            def commentsTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(usersTableKey, usersTableColumns.getTenantColumnName(), usersTableColumns.getIdentityColumnNameAndTypeMap(), iGetCurrentTenantIdFunctionInvocationFactory, "is_user_exists", schema) >> usersTableSQLDefinition
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(commentsTableKey, commentsTableColumns.getTenantColumnName(), commentsTableColumns.getIdentityColumnNameAndTypeMap(), iGetCurrentTenantIdFunctionInvocationFactory, "is_comment_exists", schema) >> commentsTableSQLDefinition
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
    def "should create all required SQL definition that creates functions that checks if records from tables in schema #tablesSchema exists when default schema is #schema"()
    {
        given:
            def builder = prepareBuilder(schema, tablesSchema)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
            def usersTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def commentsTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def isRecordBelongsToCurrentTenantFunctionDefinitionProducer = Mock(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(isRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def usersTableKey = tk("users", tablesSchema)
            def commentsTableKey = tk("comments", tablesSchema)
            def usersTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(usersTableKey)
            def commentsTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(usersTableKey, usersTableColumns.getTenantColumnName(), usersTableColumns.getIdentityColumnNameAndTypeMap(), iGetCurrentTenantIdFunctionInvocationFactory, "is_user_exists", tablesSchema) >> usersTableSQLDefinition
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(commentsTableKey, commentsTableColumns.getTenantColumnName(), commentsTableColumns.getIdentityColumnNameAndTypeMap(), iGetCurrentTenantIdFunctionInvocationFactory, "is_comment_exists", tablesSchema) >> commentsTableSQLDefinition
            0 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(_)

            result.getSqlDefinitions().contains(usersTableSQLDefinition)
            result.getSqlDefinitions().contains(commentsTableSQLDefinition)

        and: "put function objects into map"
            result.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().get(usersTableKey) == usersTableSQLDefinition
            result.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().get(commentsTableKey) == commentsTableSQLDefinition

        where:
            schema          |    tablesSchema
            null            |   "some_schema"
            "public"        |   "some_schema"
            "some_schema"   |   null
    }

    @Unroll
    def "should pass default tenant column when table does not have specified tenant column"()
    {
        given:
            def builder = prepareBuilder(schema)
                    .createRLSPolicyForTable("users", [id: "N/A"], null, "N/A")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
            def usersTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def commentsTableSQLDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            def isRecordBelongsToCurrentTenantFunctionDefinitionProducer = Mock(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(isRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def usersTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(usersTableKey)
            def commentsTableColumns = sharedSchemaContextRequest.getTableColumnsList().get(commentsTableKey)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(usersTableKey, DEFAULT_TENANT_ID_COLUMN, usersTableColumns.getIdentityColumnNameAndTypeMap(), iGetCurrentTenantIdFunctionInvocationFactory, "is_user_exists", schema) >> usersTableSQLDefinition
            1 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(commentsTableKey, commentsTableColumns.getTenantColumnName(), commentsTableColumns.getIdentityColumnNameAndTypeMap(), iGetCurrentTenantIdFunctionInvocationFactory, "is_comment_exists", schema) >> commentsTableSQLDefinition
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
            builder.createRLSPolicyForTable("users", [id: "N/A"], "tenant", "N/A")
            builder.createRLSPolicyForTable("comments", [uuid: "N/A"], "tenant_id", "N/A")
            builder.createRLSPolicyForTable("some_table", [somedid: "N/A"], "tenant_xxx_id", "N/A")
            builder.setNameForFunctionThatChecksIfRecordExistsInTable("users", "is_user_exists")
            builder.setNameForFunctionThatChecksIfRecordExistsInTable("comments", "is_comment_exists")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
            def isRecordBelongsToCurrentTenantFunctionDefinitionProducer = Mock(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(isRecordBelongsToCurrentTenantFunctionDefinitionProducer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            0 * isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(_)
            result.getSqlDefinitions().isEmpty()

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should throw an exception when there missing the function name declaration for table #table and schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable(table, [id: "N/A"], "tenant", "N/A")
            builder.createRLSPolicyForTable("comments", [uuid: "N/A"], "tenant_id", "N/A")
            builder.createSameTenantConstraintForForeignKey("comments", table, mapBuilder().put("N/A", "N/A").build(), "N/A")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            context.setIGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
            def isRecordBelongsToCurrentTenantFunctionDefinitionProducer = Mock(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
            def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(isRecordBelongsToCurrentTenantFunctionDefinitionProducer)

        when:
            tested.enrich(context, sharedSchemaContextRequest)

        then:
            def ex = thrown(MissingFunctionNameDeclarationForTableException)

        and: "message should match"
            ex.message == format("Missing function name that checks if record exists in table %1\$s and schema %2\$s", table, schema)

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

    DefaultSharedSchemaContextBuilder prepareBuilder(String schema)
    {
        prepareBuilder(schema, schema)
    }

    DefaultSharedSchemaContextBuilder prepareBuilder(String schema, String tablesSchema)
    {
        new DefaultSharedSchemaContextBuilder(schema)
                .createRLSPolicyForTable(new TableKey("users", tablesSchema), [id: "N/A"], "tenant", "N/A")
                .createRLSPolicyForTable(new TableKey("comments", tablesSchema), [uuid: "N/A"], "tenant_id", "N/A")
                .createRLSPolicyForTable(new TableKey("some_table", tablesSchema), [somedid: "N/A"], "tenant_xxx_id", "N/A")
                .createSameTenantConstraintForForeignKey(new TableKey("comments", tablesSchema), new TableKey("users", tablesSchema), mapBuilder().put("N/A", "N/A").build(), "N/A")
                .createSameTenantConstraintForForeignKey(new TableKey("some_table", tablesSchema), new TableKey("users", tablesSchema), mapBuilder().put("N/A", "N/A").build(), "N/A")
                .createSameTenantConstraintForForeignKey(new TableKey("some_table", tablesSchema), new TableKey("comments", tablesSchema), mapBuilder().put("N/A", "N/A").build(), "N/A")
                .setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey("users", tablesSchema), "is_user_exists")
                .setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey("comments", tablesSchema), "is_comment_exists")
    }
}
