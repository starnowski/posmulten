package com.github.starnowski.posmulten.postgresql.core


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
abstract class AbstractFunctionFactoryGenericItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    @Unroll
    def "should create correctly the creation and drop statements for schema #schema and function #functionName"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> functionName
            def functionDefinition = tested.produce(parameters)
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))

        when:
            jdbcTemplate.execute(functionDefinition.getCreateScript())
            def functionWasCreated = isFunctionExists(jdbcTemplate, functionName, schema)
            jdbcTemplate.execute(functionDefinition.getDropScript())
            def functionWasDeleted = !isFunctionExists(jdbcTemplate, functionName, schema)

        then:
            functionWasCreated
            functionWasDeleted

        where:
            schema      |   functionName
            null        |   "fun1"
            "public"    |   "fun1"
            "non_public_schema"       |   "fun1"
            null        |   "this_is_function"
            "public"    |   "this_is_function"
            "non_public_schema"       |   "this_is_function"
    }

    abstract protected returnTestedObject();

    abstract protected returnCorrectParametersSpyObject();
}
