package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import spock.lang.Specification
import spock.lang.Unroll

class ForeignKeysMappingSharedSchemaContextRequestValidatorTest extends Specification {

    def tested = new ForeignKeysMappingSharedSchemaContextRequestValidator()

    @Unroll
    def "should not throw any exception when foreign keys mapping is correctly defined for tests parameters: #foreignKeysTable, #primaryKeysTable, #foreignKeysMapping, #primayKeyTypeDefinition"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.createSameTenantConstraintForForeignKey(foreignKeysTable, primaryKeysTable, foreignKeysMapping, null)
            builder.createRLSPolicyForTable(primaryKeysTable, primayKeyTypeDefinition, null, null)
            SharedSchemaContextRequest request = builder.getSharedSchemaContextRequestCopy()

        when:
            tested.validate(request)

        then:
            noExceptionThrown()

        where:
            foreignKeysTable    |   primaryKeysTable    |   foreignKeysMapping              |   primayKeyTypeDefinition
            "comments"          |   "users"             |   [user_id: "id"]                 |   [id: null]
            "posts"             |   "users"             |   [user_id: "uuid"]               |   [uuid: null]
            "posts"             |   "comments"          |   [comment_id: "comment_id"]      |   [comment_id: null]
    }
}
