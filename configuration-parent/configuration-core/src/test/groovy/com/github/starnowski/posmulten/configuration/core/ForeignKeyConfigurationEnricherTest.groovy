package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
import spock.lang.Unroll

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
            def result = tested.enrich(builder, table, configuration)

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
}
