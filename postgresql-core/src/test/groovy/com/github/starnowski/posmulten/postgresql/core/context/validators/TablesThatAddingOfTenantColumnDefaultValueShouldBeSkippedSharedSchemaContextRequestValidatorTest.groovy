package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException
import spock.lang.Specification
import spock.lang.Unroll

class TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidatorTest extends Specification {

    def tested = new TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidator()

    @Unroll
    def "should not throw any exception when all tables that tenant column creation should be skipped has reference in map for tables that required rls policy creation for tests parameters: #foreignKeysTable, #primaryKeysTable, #primayKeyTypeDefinition"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.createRLSPolicyForTable(primaryKeysTable, primayKeyTypeDefinition, null, null)
            builder.skipAddingOfTenantColumnDefaultValueForTable(primaryKeysTable)
            SharedSchemaContextRequest request = builder.getSharedSchemaContextRequestCopy()

        when:
            tested.validate(request)

        then:
            noExceptionThrown()

        where:
            foreignKeysTable    |   primaryKeysTable    |   primayKeyTypeDefinition
            "comments"          |   "users"             |   [id: null]
            "posts"             |   "users"             |   [uuid: null]
            "posts"             |   "comments"          |   [comment_id: null]
            "posts"             |   "comments"          |   [comment_id: null, user: null]
    }

    @Unroll
    def "should throw exception when there is missing rls policy declaration for table #tablesForWhichTenantColumnCreationShouldBeSkipped in schema #schema for which creation of tenant column should be skipped when there are only rls policy declaration for tables #rlsTables"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder(schema)
            for (String table : rlsTables)
            {
                builder.createRLSPolicyForTable(table, [id: null], null, null)
            }
            SharedSchemaContextRequest request = builder.skipAddingOfTenantColumnDefaultValueForTable(tablesForWhichTenantColumnCreationShouldBeSkipped)
                .getSharedSchemaContextRequestCopy()

        when:
            tested.validate(request)

        then:
            def ex = thrown(MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException)

        and: "exception should have correct message"
            ex.message == expectedMessage

        and: "exception should have correctly set table key"
            ex.tableKey == new TableKey(tablesForWhichTenantColumnCreationShouldBeSkipped, schema)

        where:
            schema          |   tablesForWhichTenantColumnCreationShouldBeSkipped       |   rlsTables                   ||   expectedMessage
            null            |   "comments"                                              |   ["users", "company"]        ||  "Missing RLS policy declaration for table comments in schema null for which creation of tenant column should be skipped"
            null            |   "posts"                                                 |   ["users", "company"]        ||  "Missing RLS policy declaration for table posts in schema null for which creation of tenant column should be skipped"
            "some_schema"   |   "users"                                                 |   ["posts", "company"]        ||  "Missing RLS policy declaration for table users in schema some_schema for which creation of tenant column should be skipped"
            "some_schema"   |   "comments"                                              |   ["users", "company"]        ||  "Missing RLS policy declaration for table comments in schema some_schema for which creation of tenant column should be skipped"
    }
}
