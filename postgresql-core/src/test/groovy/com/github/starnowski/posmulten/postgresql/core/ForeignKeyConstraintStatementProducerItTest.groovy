package com.github.starnowski.posmulten.postgresql.core

import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isConstraintExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class ForeignKeyConstraintStatementProducerItTest extends Specification {

    def tested = new ForeignKeyConstraintStatementProducer()

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
            def parameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName(constraintName)
                    .withTableName(table)
                    .withTableSchema(schema)
                    .withReferenceTableKey(new TableKey(referenceTable, referenceSchema))
                    .withForeignKeyColumnMappings(foreignKeyColumnMappings).build()

        when:
            definition = tested.produce(parameters)
            jdbcTemplate.execute(definition.getCreateScript())

        then:
            isConstraintExists(jdbcTemplate, schema, table, constraintName)

        where:
            testConstraintName      |   testSchema      | testTable     |   referenceSchema |   referenceTable  |   foreignKeyColumnMappings
                "f_key_users"             |   "non_public_schema_with_composite_key"        | "posts"       |   "non_public_schema_with_composite_key"            |   "users"         |   [tenant_id : "tenant_id", user_id: "id"]
    }

    def cleanup() {
        jdbcTemplate.execute(definition.getDropScript())
        assertEquals(false, isConstraintExists(jdbcTemplate, schema, table, constraintName))
    }
}