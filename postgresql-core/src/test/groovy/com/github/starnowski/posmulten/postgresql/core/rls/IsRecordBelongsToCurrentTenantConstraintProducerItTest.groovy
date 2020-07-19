package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.test.utils.RandomString
import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class IsRecordBelongsToCurrentTenantConstraintProducerItTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantConstraintProducer()

    @Autowired
    JdbcTemplate jdbcTemplate

    def definition
    def schema
    def table
    def constraintName

    @Unroll
    def "should return statement that adds '#testConstraintName' constraint to table (#testTable) and schema (#testSchema) with condition '#conditionStatement'"()
    {
        given:
            schema = testSchema
            table = testTable
            constraintName = testConstraintName
            assertEquals(false, isAnyRecordExists(jdbcTemplate, createSelectStatement(schema, table, constraintName)))
            IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                    {
                        conditionStatement
                    }
            def parameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                    .withConstraintName(constraintName)
                    .withTableName(table)
                    .withTableSchema(schema)
                    .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                    .withPrimaryColumnsValuesMap(primaryColumnsValuesMap).build()
        when:
            definition = tested.produce(parameters)
            jdbcTemplate.execute(definition.getCreateScript())

        then:
            isAnyRecordExists(jdbcTemplate, createSelectStatement(schema, table, constraintName))

        where:
            testConstraintName      |   testSchema              | testTable     |   primaryColumnsValuesMap                                                     |   conditionStatement
            "sss"                   |   null                    | "users"       |   [id : randomFAV()]                                                          |   "tenant_id = 'dsasdf'"
            "sss"                   |   "public"                | "users"       |   [id : randomFAV()]                                                          |   "Cast(current_setting('some.boolean.value') as boolean)"
            "sss"                   |   "non_public_schema"     | "users"       |   [id : randomFAV()]                                                          |   "tenant_id = 'dsasdf'"
            "user_belongs_tt"       |   "non_public_schema"     | "users"       |   [id : randomFAV()]                                                          |   "Cast(current_setting('boolean.value2') as boolean)"
            "user_belongs_tt"       |   "non_public_schema"     | "users"       |   [id : randomFAV()]                                                          |   "tenant_id = 'dsasdf'"
            "user_belongs_tt"       |   "non_public_schema"     | "posts"       |   [user_id : randomFAV()]                                                     |   "Cast(current_setting('bool.value2') as boolean)"
            "user_belongs_tt"       |   "non_public_schema"     | "posts"       |   [user_id : randomFAV()]                                                     |   "tenant_id = 'dsasdf'"
            "does_comment_from_t"   |   "non_public_schema"     | "comments"    |   [parent_comment_id : randomFAV(), parent_comment_user_id: randomFAV()]      |   "tenant = 'dsasdf'"
    }

    String createSelectStatement(String schema, String table, String constraintName)
    {
        def template = "SELECT 1\n" +
        "\t\tFROM information_schema.table_constraints\n" +
        "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'"
        String.format(template, schema == null ? "public" : schema, table, constraintName)
    }

    FunctionArgumentValue randomFAV()
    {
        def randomString = new RandomString(5, new Random(), RandomString.lower)
        forReference(randomString.nextString())
    }

    def cleanup() {
        jdbcTemplate.execute(definition.getDropScript())
        assertEquals(false, isAnyRecordExists(jdbcTemplate, createSelectStatement(schema, table, constraintName)))
    }
}
