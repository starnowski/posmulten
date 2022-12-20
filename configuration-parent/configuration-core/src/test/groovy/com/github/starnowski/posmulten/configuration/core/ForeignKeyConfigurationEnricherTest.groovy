package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import spock.lang.Unroll

import static java.util.Optional.of

class ForeignKeyConfigurationEnricherTest extends AbstractBaseTest {

    def tested = new ForeignKeyConfigurationEnricher()

    @Unroll
    def "should add foreign key constraint for the table #table, foreign table #foreignKeyTable, foreign keys and primary keys mapping #foreignKeyPrimaryKeyColumnsMappings with name #constraintName"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def configuration = new ForeignKeyConfiguration()
                    .setTableName(foreignKeyTable)
                    .setForeignKeyPrimaryKeyColumnsMappings(foreignKeyPrimaryKeyColumnsMappings)
                    .setConstraintName(constraintName)

        when:
            def result = tested.enrich(builder, table, null, configuration)

        then:
            result == builder
            1 * builder.createSameTenantConstraintForForeignKey(table, foreignKeyTable, foreignKeyPrimaryKeyColumnsMappings, constraintName) >> builder

        and: "do not invoke builder with other methods"
            0 * builder._

        where:
            table   |   foreignKeyTable |   foreignKeyPrimaryKeyColumnsMappings                 |   constraintName
            "posts" |   "users"         |   [user_id: "id"]                                     |   "posts_users_fk_const"
            "posts" |   "comments"      |   [comment_id: "id", comment_uuid: "comment_uuid"]    |   "posts_comments_fk_const"
            "users" |   "comments"      |   [comment_id: "uuid"]                                |   "comments_xxxx_fk_const"
    }

    @Unroll
    def "should add foreign key constraint for the table #table in schema #tableSchema, foreign table #foreignKeyTable in schema #foreignKeySchema, foreign keys and primary keys mapping #foreignKeyPrimaryKeyColumnsMappings with name #constraintName, for default schema #defaultSchema"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def request = Mock(SharedSchemaContextRequest)
            def configuration = new ForeignKeyConfiguration()
                    .setTableName(foreignKeyTable)
                    .setTableSchema(foreignKeySchema)
                    .setForeignKeyPrimaryKeyColumnsMappings(foreignKeyPrimaryKeyColumnsMappings)
                    .setConstraintName(constraintName)

        when:
            def result = tested.enrich(builder, table, tableSchema, configuration)

        then:
            result == builder
            1 * builder.getSharedSchemaContextRequestCopy() >> request
            1 * request.getDefaultSchema() >> defaultSchema
            1 * builder.createSameTenantConstraintForForeignKey(expectedTableKey, expectedForeighTableKey, foreignKeyPrimaryKeyColumnsMappings, constraintName) >> builder

        and: "do not invoke builder with other methods"
            0 * builder._

        where:
            table   |   tableSchema     |   foreignKeyTable |   foreignKeySchema                |   defaultSchema   |   foreignKeyPrimaryKeyColumnsMappings                 |   constraintName              ||  expectedTableKey            ||  expectedForeighTableKey
            "posts" |   null            |   "users"         |   Optional.ofNullable("XXX")      |   "public"        |   [user_id: "id"]                                     |   "posts_users_fk_const"      ||  tk("posts", "public")       ||  tk("users", "XXX")
            "posts" |   null            |   "comments"      |   Optional.ofNullable("schema")   |   "public"        |   [comment_id: "id", comment_uuid: "comment_uuid"]    |   "posts_comments_fk_const"   ||  tk("posts", "public")       ||  tk("comments", "schema")
            "users" |   null            |   "comments"      |   Optional.ofNullable(null)       |   "public"        |   [comment_id: "uuid"]                                |   "comments_xxxx_fk_const"    ||  tk("users", "public")       ||  tk("comments", null)
            "users" |   of("different") |   "comments"      |   null                            |   null            |   [comment_id: "uuid"]                                |   "comments_xxxx_fk_const"    ||  tk("users", "different")    ||  tk("comments", null)
            "users" |   of("different") |   "comments"      |   null                            |   "main_s"        |   [comment_id: "uuid"]                                |   "comments_xxxx_fk_const"    ||  tk("users", "different")    ||  tk("comments", "main_s")
            "users" |   of("different") |   "comments"      |   Optional.ofNullable("schema")   |   null            |   [comment_id: "uuid"]                                |   "comments_xxxx_fk_const"    ||  tk("users", "different")    ||  tk("comments", "schema")
    }

    private static TableKey tk(String table, String schema){
        new TableKey(table, schema)
    }
}
