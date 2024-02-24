package com.github.starnowski.posmulten.postgresql.core

import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.util.SqlUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isConstraintExists
import static org.junit.Assert.assertEquals

@Stepwise
@SpringBootTest(classes = [TestApplication.class])
class ForeignKeyConstraintStatementProducerItTest extends Specification {

    def tested = new ForeignKeyConstraintStatementProducer()

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    SqlUtils sqlUtils

    def definition

    @Unroll
    def "should return statement that adds '#testConstraintName' constraint to table (#testTable) and schema (#testSchema) with condition '#conditionStatement'"()
    {
        given:
            System.out.println("FIRST FEATURE")
            assertEquals(false, isConstraintExists(jdbcTemplate, testSchema, testTable, testConstraintName))
            def parameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName(testConstraintName)
                    .withTableName(testTable)
                    .withTableSchema(testSchema)
                    .withReferenceTableKey(new TableKey(referenceTable, referenceSchema))
                    .withForeignKeyColumnMappings(foreignKeyColumnMappings).build()
            definition = tested.produce(parameters)

        when:
            jdbcTemplate.execute(definition.getCreateScript())

        then:
            isConstraintExists(jdbcTemplate, testSchema, testTable, testConstraintName)

        and: "and all checking statement should pass"
            sqlUtils.assertAllCheckingStatementsArePassing(definition)

        where:
            testConstraintName          |   testSchema                                      | testTable     |   referenceSchema                                     |   referenceTable  |   foreignKeyColumnMappings
            "f_key_users"               |   "non_public_schema_with_composite_key"          | "posts"       |   "non_public_schema_with_composite_key"              |   "users"         |   [tenant_id : "tenant_id", user_id: "id"]
            "f_key_users_groups_to_users"               |   "non_public_schema_with_composite_key"          | "users_groups"       |   "non_public_schema_with_composite_key"              |   "users"         |   [tenant_id : "tenant_id", user_id: "id"]
    }

    @Unroll
    def "should drop constraint #testConstraintName for schema #testSchema and table #testTable based on correct drop statement"(){
        given:
            System.out.println("SECOND FEATURE")
            assertEquals(true, isConstraintExists(jdbcTemplate, testSchema, testTable, testConstraintName))
            def parameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName(testConstraintName)
                    .withTableName(testTable)
                    .withTableSchema(testSchema)
                    .withReferenceTableKey(new TableKey(referenceTable, referenceSchema))
                    .withForeignKeyColumnMappings(foreignKeyColumnMappings).build()
            definition = tested.produce(parameters)

        when:
            jdbcTemplate.execute(definition.getDropScript())

        then:
            !isConstraintExists(jdbcTemplate, testSchema, testTable, testConstraintName)

        and: "and all checking statement should return zero as result"
            sqlUtils.assertAllResultForCheckingStatementsAreEqualZero(definition)

        where:
            testConstraintName          |   testSchema                                      | testTable     |   referenceSchema                                     |   referenceTable  |   foreignKeyColumnMappings
            "f_key_users"               |   "non_public_schema_with_composite_key"          | "posts"       |   "non_public_schema_with_composite_key"              |   "users"         |   [tenant_id : "tenant_id", user_id: "id"]
            "f_key_users_groups_to_users"               |   "non_public_schema_with_composite_key"          | "users_groups"       |   "non_public_schema_with_composite_key"              |   "users"         |   [tenant_id : "tenant_id", user_id: "id"]
    }

}