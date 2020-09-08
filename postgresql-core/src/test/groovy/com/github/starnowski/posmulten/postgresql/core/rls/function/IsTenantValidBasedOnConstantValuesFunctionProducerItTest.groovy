package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.StatementCallback
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString

@SpringBootTest(classes = [TestApplication.class])
class IsTenantValidBasedOnConstantValuesFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new IsTenantValidBasedOnConstantValuesFunctionProducer()

    def functionName
    def schema
    def functionDefinition

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' with argument type '#argumentType' (null means 'text') with invalid tenant values'#invalidTenantValues'" () {
        expect:
        tested.produce(new IsTenantValidBasedOnConstantValuesFunctionProducerParameters(testFunctionName, testSchema, new HashSet<String>(invalidTenantValues), argumentType)).getCreateScript() == expectedStatement

        where:
        testSchema              |   testFunctionName            |   invalidTenantValues     |   argumentType        ||  invalidValues                               |   validValues
        null                    |   "is_tenant_valid"           |   ["3325", "adfzxcvz"]    |   null                || [forString("3325"), forString("adfzxcvz")]   |   [forString("afdxzv"), forString("1234asdf")]
        "public"                |   "is_tenant_valid"           |   ["3325", "adfzxcvz"]    |   null                || //TODO
        "non_public_schema"     |   "is_tenant_valid"           |   ["3325", "adfzxcvz"]    |   null                ||
        null                    |   "is_valid_ten"              |   ["3325", "adfzxcvz"]    |   null                ||
        "public"                |   "valid_tenant"              |   ["dgfsg", "433"]        |   "VARCHAR(32)"       ||
        "schema2"               |   "tenant_is_correct"         |   ["66", "12", "0"]       |   "INTEGER"           ||
    }

    def returnSelectStatementAsBoolean(String selectStatement)
    {
        return jdbcTemplate.execute(new StatementCallback<Boolean>() {
            @Override
            Boolean doInStatement(Statement statement) throws SQLException, DataAccessException {
                ResultSet rs = statement.executeQuery(selectStatement)
                rs.next()
                return rs.getBoolean(1)
            }
        })
    }
}
