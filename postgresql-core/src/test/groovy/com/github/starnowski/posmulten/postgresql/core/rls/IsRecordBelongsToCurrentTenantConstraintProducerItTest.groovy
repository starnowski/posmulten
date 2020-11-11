package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isConstraintExists
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
            assertEquals(false, isConstraintExists(jdbcTemplate, schema, table, constraintName))
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
            isConstraintExists(jdbcTemplate, schema, table, constraintName)

        where:
            testConstraintName      |   testSchema              | testTable     |   primaryColumnsValuesMap                                                                     |   conditionStatement
            "sss"                   |   null                    | "users"       |   [id : forReference("id")]                                                                   |   "tenant_id = 'dsasdf'"
            "sss"                   |   "public"                | "users"       |   [id : forReference("id")]                                                                   |   "Cast(current_setting('some.boolean.value') as boolean)"
            "sss"                   |   "non_public_schema"     | "users"       |   [id : forReference("id")]                                                                   |   "tenant_id = 'dsasdf'"
            "user_belongs_tt"       |   "non_public_schema"     | "users"       |   [id : forReference("id")]                                                                   |   "Cast(current_setting('boolean.value2') as boolean)"
            "user_belongs_tt"       |   "non_public_schema"     | "users"       |   [id : forReference("id")]                                                                   |   "tenant_id = 'dsasdf'"
            "user_belongs_tt"       |   "non_public_schema"     | "posts"       |   [id : forReference("user_id")]                                                              |   "Cast(current_setting('bool.value2') as boolean)"
            "user_belongs_tt"       |   "non_public_schema"     | "posts"       |   [id : forReference("user_id")]                                                              |   "tenant_id = 'dsasdf'"
            "does_comment_from_t"   |   "non_public_schema"     | "comments"    |   [id : forReference("parent_comment_id"), user_id: forReference("parent_comment_user_id")]   |   "tenant = 'dsasdf'"
    }

    def cleanup() {
        jdbcTemplate.execute(definition.getDropScript())
        assertEquals(false, isConstraintExists(jdbcTemplate, schema, table, constraintName))
    }
}
