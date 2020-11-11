package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableException
import spock.lang.Specification
import spock.lang.Unroll

class CreateTenantColumnTableMappingSharedSchemaContextRequestValidatorTest extends Specification {

    def tested = new CreateTenantColumnTableMappingSharedSchemaContextRequestValidator()

    @Unroll
    def "should not throw any exception when all tables that required tenant column creation has reference in map for tables that required rls policy creation for tests parameters: #foreignKeysTable, #primaryKeysTable, #foreignKeysMapping, #primayKeyTypeDefinition"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.createRLSPolicyForTable(primaryKeysTable, primayKeyTypeDefinition, null, null)
            builder.createTenantColumnForTable(primaryKeysTable)
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
    def "should throw exception when there is missing rls policy declaration for table #tableRequiredTenantColumnCreation in schema #schema for which creation of tenant column was requested when there are only rls policy declaration for tables #rlsTables"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder(schema)
            for (String table : rlsTables)
            {
                builder.createRLSPolicyForTable(table, [id: null], null, null)
            }
            builder.createTenantColumnForTable(tableRequiredTenantColumnCreation)
            SharedSchemaContextRequest request = builder.getSharedSchemaContextRequestCopy()

        when:
            tested.validate(request)

        then:
            def ex = thrown(MissingRLSPolicyDeclarationForTableException)

        and: "exception should have correct message"
            ex.message == expectedMessage

        and: "exception should have correctly set table key"
            ex.tableKey == new TableKey(tableRequiredTenantColumnCreation, schema)

        where:
            schema          |   tableRequiredTenantColumnCreation       |   rlsTables                   ||   expectedMessage
            null            |   "comments"                              |   ["users", "company"]        ||  "Missing RLS policy declaration for table comments in schema null for which creation of tenant column was requested"
            null            |   "posts"                                 |   ["users", "company"]        ||  "Missing RLS policy declaration for table posts in schema null for which creation of tenant column was requested"
            "some_schema"   |   "users"                                 |   ["posts", "company"]        ||  "Missing RLS policy declaration for table users in schema some_schema for which creation of tenant column was requested"
            "some_schema"   |   "comments"                              |   ["users", "company"]        ||  "Missing RLS policy declaration for table comments in schema some_schema for which creation of tenant column was requested"
    }

}
