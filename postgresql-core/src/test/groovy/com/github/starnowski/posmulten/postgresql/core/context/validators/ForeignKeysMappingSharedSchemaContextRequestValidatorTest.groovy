package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
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
    def "should throw exception when foreign keys mapping does not match to defined primary keys for tests parameters: #schema, #foreignKeysTable, #primaryKeysTable, #foreignKeysMapping, #primayKeyTypeDefinition"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createSameTenantConstraintForForeignKey(foreignKeysTable, primaryKeysTable, foreignKeysMapping, null)
            builder.createRLSPolicyForTable(primaryKeysTable, primayKeyTypeDefinition, null, null)
            SharedSchemaContextRequest request = builder.getSharedSchemaContextRequestCopy()

        when:
            tested.validate(request)

        then:
            def ex = thrown(IncorrectForeignKeysMappingException)

        and: "exception should have correct message"
            ex.message == expectedMessage

        and: "exception should have correct properties"
            ex.foreignTableKey == new TableKey(foreignKeysTable, schema)
            ex.primaryTableKey == new TableKey(primaryKeysTable, schema)
            ex.foreignTableKey == new HashSet<>(foreignKeysMapping.values())
            ex.primaryKeys == primayKeyTypeDefinition.keySet()

        where:
            schema              |   foreignKeysTable    |   primaryKeysTable    |   foreignKeysMapping                                          |   primayKeyTypeDefinition             ||  expectedMessage
            null                |   "comments"          |   "users"             |   [user_id: "id"]                                             |   [uuid: null]                        ||  "There is mismatch between foreign keys column mapping (id) in comments table and primary keys column declaration (uuid) for users table"
            null                |   "posts"             |   "users"             |   [user_id: "uuid"]                                           |   [id: null]                          ||  "There is mismatch between foreign keys column mapping (uuid) in posts table and primary keys column declaration (id) for users table"
            null                |   "posts"             |   "comments"          |   [comment_id: "comment_id", comment_user: "user_id"]         |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (comment_id, user_id) in posts table and primary keys column declaration (comment_id, user) for users table"
            null                |   "posts"             |   "comments"          |   [comment_id: "id", comment_user: "user_id"]                 |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (id, user_id) in posts table and primary keys column declaration (comment_id, user) for users table"
            "public"            |   "comments"          |   "users"             |   [user_id: "id"]                                             |   [uuid: null]                        ||  "There is mismatch between foreign keys column mapping (id) in public.comments table and primary keys column declaration (uuid) for public.users table"
            "public"            |   "posts"             |   "users"             |   [user_id: "uuid"]                                           |   [id: null]                          ||  "There is mismatch between foreign keys column mapping (uuid) in public.posts table and primary keys column declaration (id) for public.users table"
            "public"            |   "posts"             |   "comments"          |   [comment_id: "comment_id", comment_user: "user_id"]         |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (comment_id, user_id) in public.posts table and primary keys column declaration (comment_id, user) for public.users table"
            "public"            |   "posts"             |   "comments"          |   [comment_id: "id", comment_user: "user_id"]                 |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (id, user_id) in public.posts table and primary keys column declaration (comment_id, user) for public.users table"
            "some_schema"       |   "comments"          |   "users"             |   [user_id: "id"]                                             |   [uuid: null]                        ||  "There is mismatch between foreign keys column mapping (id) in some_schema.comments table and primary keys column declaration (uuid) for some_schema.users table"
            "some_schema"       |   "posts"             |   "users"             |   [user_id: "uuid"]                                           |   [id: null]                          ||  "There is mismatch between foreign keys column mapping (uuid) in some_schema.posts table and primary keys column declaration (id) for some_schema.users table"
            "some_schema"       |   "posts"             |   "comments"          |   [comment_id: "comment_id", comment_user: "user_id"]         |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (comment_id, user_id) in some_schema.posts table and primary keys column declaration (comment_id, user) for some_schema.users table"
            "some_schema"       |   "posts"             |   "comments"          |   [comment_id: "id", comment_user: "user_id"]                 |   [comment_id: null, user: null]      ||  "There is mismatch between foreign keys column mapping (id, user_id) in some_schema.posts table and primary keys column declaration (comment_id, user) for some_schema.users table"
    }
}
