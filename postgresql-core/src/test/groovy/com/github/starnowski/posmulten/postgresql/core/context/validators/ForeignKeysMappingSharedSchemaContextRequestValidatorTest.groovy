package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.IncorrectForeignKeysMappingException
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
            foreignKeysTable    |   primaryKeysTable    |   foreignKeysMapping                                          |   primayKeyTypeDefinition
            "comments"          |   "users"             |   [user_id: "id"]                                             |   [id: null]
            "posts"             |   "users"             |   [user_id: "uuid"]                                           |   [uuid: null]
            "posts"             |   "comments"          |   [comment_id: "comment_id"]                                  |   [comment_id: null]
            "posts"             |   "comments"          |   [comment_id: "comment_id", comment_user: "user"]            |   [comment_id: null, user: null]
    }

    @Unroll
    def "should throw exception when foreign keys mapping does not match to defined primary keys for tests parameters: #foreignKeysTable, #primaryKeysTable, #foreignKeysMapping, #primayKeyTypeDefinition"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.createSameTenantConstraintForForeignKey(foreignKeysTable, primaryKeysTable, foreignKeysMapping, null)
            builder.createRLSPolicyForTable(primaryKeysTable, primayKeyTypeDefinition, null, null)
            SharedSchemaContextRequest request = builder.getSharedSchemaContextRequestCopy()

        when:
            tested.validate(request)

        then:
            def ex = thrown(IncorrectForeignKeysMappingException)

        and: "exception should have correct message"
            ex.message == expectedMessage

        where:
            foreignKeysTable    |   primaryKeysTable    |   foreignKeysMapping                                          |   primayKeyTypeDefinition             ||  expectedMessage
            "comments"          |   "users"             |   [user_id: "id"]                                             |   [uuid: null]                        ||  "There is mismatch between foreign keys column mapping (id) in comments table and primary keys column declaration (uuid) for users table"
            "posts"             |   "users"             |   [user_id: "uuid"]                                           |   [id: null]                          ||  "There is mismatch between foreign keys column mapping (uuid) in posts table and primary keys column declaration (id) for users table"
            "posts"             |   "comments"          |   [comment_id: "comment_id", comment_user: "user_id"]         |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (comment_id, user_id) in posts table and primary keys column declaration (comment_id, user) for users table"
            "posts"             |   "comments"          |   [comment_id: "id", comment_user: "user_id"]                 |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (id, user_id) in posts table and primary keys column declaration (comment_id, user) for users table"
    }
}
