package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.RandomString
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isAnyRecordExists
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static org.junit.Assert.assertEquals

class IsRecordBelongsToCurrentTenantConstraintProducerItTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantConstraintProducer()

    @Autowired
    JdbcTemplate jdbcTemplate

    def definition

    @Unroll
    def "should return statement that adds '#constraintName' constraint to table (#table) and schema (#schema) with condition '#conditionStatement'"()
    {
        given:
            assertEquals(false, isAnyRecordExists(jdbcTemplate, createSelectStatement(schema, table, constraintName)))
            IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                    {
                        conditionStatement
                    }
            Map<String, FunctionArgumentValue> primaryColumnsValuesMap = generateRandomPrimaryColumnsValuesMap()
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
            constraintName      |   schema              | table     |   conditionStatement
            "sss"               |   null                | "users"   |   "tenant_id = 'dsasdf'"
            "sss"               |   "public"            | "users"   |   "Cast(current_setting('some.boolean.value') as boolean)"
            "sss"               |   "non_public_schema" | "users"   |   "tenant = 'dsasdf'"
            "user_belongs_tt"   |   "non_public_schema" | "users"   |   "Cast(current_setting('boolean.value2') as boolean)"
            "user_belongs_tt"   |   "non_public_schema" | "users"   |   "tenant_id = 'dsasdf'"
    }

    String createSelectStatement(String schema, String table, String constraintName)
    {
        def template = "SELECT 1\n" +
        "\t\tFROM information_schema.table_constraints\n" +
        "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'"
        String.format(template, schema == null ? "public" : schema, table, constraintName)
    }

    Map<String, FunctionArgumentValue> generateRandomPrimaryColumnsValuesMap()
    {
        def randomString = new RandomString(5, new Random(), RandomString.lower)
        def random = new Random()
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>()
        def mapSize = random.nextInt(5) + 1
        for (int i = 0; i < mapSize; i++)
        {
            primaryColumnsValuesMap.put(randomString.nextString(), forReference(randomString.nextString()))
        }
        primaryColumnsValuesMap
    }

    def cleanup() {
        jdbcTemplate.execute(functionDefinition.getDropScript())
        //TODO
        assertEquals(false, isAnyRecordExists(jdbcTemplate, createSelectStatement(schema, table, constraintName)))
    }
}
