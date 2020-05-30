package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class IsRecordBelongsToCurrentTenantProducerItTest extends Specification {

    private static String VALID_CURRENT_TENANT_ID_PROPERTY_NAME = "c.c_ten"
    IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory =
            {
                "current_setting('" + VALID_CURRENT_TENANT_ID_PROPERTY_NAME + "')"
            }
    String schema
    String functionName
    def tested = new IsRecordBelongsToCurrentTenantProducer()
    def functionDefinition

    @Autowired
    JdbcTemplate jdbcTemplate

    @Unroll
    def "for function name '#testFunctionName' for schema '#testSchema', table #recordTableName in schema #recordSchemaName that compares values for columns #keyColumnsPairs and tenant column #tenantColumnPair, should generate statement that creates function" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
            def parameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                    .withSchema(testSchema)
                    .withFunctionName(testFunctionName)
                    .withRecordTableName(recordTableName)
                    .withRecordSchemaName(recordSchemaName)
                    .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
                    .withTenantColumnPair(tenantColumnPair)
                    .withKeyColumnsPairsList(keyColumnsPairs).build()

        when:
            functionDefinition = tested.produce(parameters)
            jdbcTemplate.execute(functionDefinition.getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        where:
            testSchema              |   testFunctionName                        |   recordTableName     |   recordSchemaName    |   tenantColumnPair                                            |   keyColumnsPairs
            null                    |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   pairOfColumnWithType("tenant_id", "text")                   |   [pairOfColumnWithType("id", "bigint")]
            "public"                |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   pairOfColumnWithType("tenant_id", "text")                   |   [pairOfColumnWithType("id", "bigint")]
            "public"                |   "is_user_belongs_to_current_tenant"     |   "users"             |   "public"            |   pairOfColumnWithType("tenant_id", "text")                   |   [pairOfColumnWithType("id", "bigint")]
            "non_public_schema"     |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   pairOfColumnWithType("tenant_id", "text")                   |   [pairOfColumnWithType("id", "bigint")]
            "non_public_schema"     |   "is_user_belongs_to_current_tenant"     |   "users"             |   "public"            |   pairOfColumnWithType("tenant_id", "text")                   |   [pairOfColumnWithType("id", "bigint")]
            "non_public_schema"     |   "is_user_belongs_to_current_tenant"     |   "users"             |   "non_public_schema" |   pairOfColumnWithType("tenant_id", "text")                   |   [pairOfColumnWithType("id", "bigint")]
            "public"                |   "is_comments_belongs_to_current_tenant" |   "comments"          |   "public"            |   pairOfColumnWithType("tenant", "character varying(255)")    |   [pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint")]
            "non_public_schema"     |   "is_comments_belongs_to_current_tenant" |   "comments"          |   "public"            |   pairOfColumnWithType("tenant", "character varying(255)")    |   [pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint")]
            "non_public_schema"     |   "is_comments_belongs_to_current_tenant" |   "comments"          |   "non_public_schema" |   pairOfColumnWithType("tenant", "character varying(255)")    |   [pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint")]
    }

    def cleanup() {
        jdbcTemplate.execute(functionDefinition.getDropScript())
        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }

}
