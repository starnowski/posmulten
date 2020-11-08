package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
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

}
