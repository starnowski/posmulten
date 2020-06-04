package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.StatementCallback
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.CLEAR_DATABASE_SCRIPT_PATH
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forNumeric
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType
import static org.junit.Assert.assertEquals
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED

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
    def "for function name '#testFunctionName' for schema '#testSchema', table #recordTableName in schema #recordSchemaName that compares values for columns #keyColumnsPairs and tenant column #tenantColumn, should generate statement that creates function" () {
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
                    .withTenantColumn(tenantColumn)
                    .withKeyColumnsPairsList(keyColumnsPairs).build()

        when:
            functionDefinition = tested.produce(parameters)
            jdbcTemplate.execute(functionDefinition.getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        where:
            testSchema              |   testFunctionName                        |   recordTableName     |   recordSchemaName    |   tenantColumn                    |   keyColumnsPairs
            null                    |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   "tenant_id"                     |   [pairOfColumnWithType("id", "bigint")]
            "public"                |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   "tenant_id"                     |   [pairOfColumnWithType("id", "bigint")]
            "public"                |   "is_user_belongs_to_current_tenant"     |   "users"             |   "public"            |   "tenant_id"                     |   [pairOfColumnWithType("id", "bigint")]
            "non_public_schema"     |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   "tenant_id"                     |   [pairOfColumnWithType("id", "bigint")]
            "non_public_schema"     |   "is_user_belongs_to_current_tenant"     |   "users"             |   "public"            |   "tenant_id"                     |   [pairOfColumnWithType("id", "bigint")]
            "non_public_schema"     |   "is_user_belongs_to_current_tenant"     |   "users"             |   "non_public_schema" |   "tenant_id"                     |   [pairOfColumnWithType("id", "bigint")]
            "public"                |   "is_comments_belongs_to_current_tenant" |   "comments"          |   "public"            |   "tenant"                        |   [pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint")]
            "non_public_schema"     |   "is_comments_belongs_to_current_tenant" |   "comments"          |   "public"            |   "tenant"                        |   [pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint")]
            "non_public_schema"     |   "is_comments_belongs_to_current_tenant" |   "comments"          |   "non_public_schema" |   "tenant"                        |   [pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint")]
    }

    @Unroll
    @SqlGroup([
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "insert-unrelated-data.sql",
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = AFTER_TEST_METHOD)])
    def "for table #recordTableName in schema #recordSchemaName that compares values for columns #keyColumnsPairs and tenant column #tenantColumn, should create function which invocation would return expected result : #expectedBooleanValue for tenant #testCurrentTenantIdValue and user id #testUsersId" () {
        given:
            functionName = "is_user_belongs_to_current_tenant"
            schema = "public"
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
            def parameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                    .withSchema(schema)
                    .withFunctionName(functionName)
                    .withRecordTableName(recordTableName)
                    .withRecordSchemaName(recordSchemaName)
                    .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
                    .withTenantColumn("tenant_id")
                    .withKeyColumnsPairsList(keyColumnsPairs).build()
            Map<String, FunctionArgumentValue> map = new HashMap<>();
            map.put("id", forNumeric(String.valueOf(testUsersId)))

        when:
            functionDefinition = tested.produce(parameters)
            jdbcTemplate.execute(functionDefinition.getCreateScript())
            System.out.println(functionDefinition.getCreateScript())

        then:
            getBooleanResultForSelectStatement(testCurrentTenantIdValue, returnTestedSelectStatement(functionDefinition.returnIsRecordBelongsToCurrentTenantFunctionInvocation(map))) == expectedBooleanValue

        where:
            recordTableName     |   recordSchemaName    |   keyColumnsPairs                         |   testCurrentTenantIdValue    |   testUsersId || expectedBooleanValue
            "users"             |   null                |   [pairOfColumnWithType("id", "bigint")]  |   "primary_tenant"            |   1           ||  true
            "users"             |   null                |   [pairOfColumnWithType("id", "bigint")]  |   "secondary_tenant"          |   2           ||  true
            "users"             |   "public"            |   [pairOfColumnWithType("id", "bigint")]  |   "primary_tenant"            |   3           ||  true
            "users"             |   null                |   [pairOfColumnWithType("id", "bigint")]  |   "secondary_tenant"          |   1           ||  false
            "users"             |   null                |   [pairOfColumnWithType("id", "bigint")]  |   "sdfafdsfa"                 |   1           ||  false
            "users"             |   null                |   [pairOfColumnWithType("id", "bigint")]  |   "secondary_tenant"          |   3           ||  false
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "third_tenant"              |   1           ||  true
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "third_tenant"              |   2           ||  true
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "primary_tenant"            |   3           ||  true
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "third_tenant"              |   4           ||  true
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "primary_tenant"            |   1           ||  false
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "primary_tenant"            |   2           ||  false
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "third_tenant"              |   3           ||  false
            "users"             |   "non_public_schema" |   [pairOfColumnWithType("id", "bigint")]  |   "primary_tenant"            |   4           ||  false
    }

    def getBooleanResultForSelectStatement(String propertyValue, String selectStatement)
    {
        return jdbcTemplate.execute(new StatementCallback<Boolean>() {
            @Override
            Boolean doInStatement(Statement statement) throws SQLException, DataAccessException {
                statement.execute("SET " + VALID_CURRENT_TENANT_ID_PROPERTY_NAME + " = '" + propertyValue + "';")
                ResultSet rs = statement.executeQuery(selectStatement)
                rs.next()
                return rs.getBoolean(1)
            }
        })
    }

    def returnTestedSelectStatement(String functionInvocation)
    {
        "SELECT " + functionInvocation
    }

    def cleanup() {
        jdbcTemplate.execute(functionDefinition.getDropScript())
        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }

}
