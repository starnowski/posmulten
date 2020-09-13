package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsTenantIdentifierValidConstraintProducerParameters.builder
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isConstraintExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class IsTenantIdentifierValidConstraintProducerItTest extends Specification {

    def tested = new IsTenantIdentifierValidConstraintProducer()

    @Autowired
    JdbcTemplate jdbcTemplate

    def definition
    def schema
    def table
    def constraintName

    @Unroll
    def "should return statement that adds '#testConstraintName' constraint to table (#testTable) and schema (#testSchema) with condition '#conditionStatement' and tenant column #tenantColumn"()
    {
        given:
            schema = testSchema
            table = testTable
            constraintName = testConstraintName
            assertEquals(false, isConstraintExists(jdbcTemplate, schema, table, constraintName))
            IIsTenantValidFunctionInvocationFactory factory =
                    {
                        conditionStatement
                    }
            def parameters = builder()
                    .withConstraintName(constraintName)
                    .withTableName(table)
                    .withTableSchema(schema)
                    .withIIsTenantValidFunctionInvocationFactory(factory)
                    .withTenantColumnName(tenantColumn).build()
        when:
            definition = tested.produce(parameters)
            jdbcTemplate.execute(definition.getCreateScript())

        then:
            isConstraintExists(jdbcTemplate, schema, table, constraintName)

        where:
            testConstraintName      |   testSchema              | testTable     |   tenantColumn    |   conditionStatement
            "sss"                   |   null                    | "users"       |   "tenant_id"     |   "tenant_id = 'dsasdf'"
            "sss"                   |   "public"                | "users"       |   "tenant_id"     |   "tenant_id <> 'asdfjlzvz[HALA'"
            "sss"                   |   "non_public_schema"     | "users"       |   "tenant_id"     |   "tenant_id = 'dsasdf'"
            "user_belongs_tt"       |   "non_public_schema"     | "users"       |   "tenant_id"     |   "id <> 3344"
            "user_belongs_tt"       |   "non_public_schema"     | "users"       |   "tenant_id"     |   "tenant_id = 'dsasdf'"
            "user_belongs_tt"       |   "non_public_schema"     | "posts"       |   "tenant_id"     |   "id = 5544"
            "user_belongs_tt"       |   "non_public_schema"     | "posts"       |   "tenant_id"     |   "tenant_id <> 'SSSAA'"
            "does_comment_from_t"   |   "non_public_schema"     | "comments"    |   "tenant"        |   "tenant = 'gggfff'"
    }

    def cleanup() {
        jdbcTemplate.execute(definition.getDropScript())
        assertEquals(false, isConstraintExists(jdbcTemplate, schema, table, constraintName))
    }
}
