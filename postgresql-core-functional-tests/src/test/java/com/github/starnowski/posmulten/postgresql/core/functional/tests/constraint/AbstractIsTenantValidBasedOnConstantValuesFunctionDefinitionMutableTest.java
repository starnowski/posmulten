package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.TestNGSpringContextWithoutGenericTransactionalSupportTests;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducerParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.Test;

import java.util.HashSet;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isFunctionExists;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.selectAndReturnFirstRecordAsBoolean;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

/**
 * The purpose of this test is to check if the function body is going to be updated even if the function is defined as IMMUTABLE.
 * It might be considered as conformance tests for the Postgresql database engine that is currently used in tests.
 */
public abstract class AbstractIsTenantValidBasedOnConstantValuesFunctionDefinitionMutableTest extends TestNGSpringContextWithoutGenericTransactionalSupportTests {

    private IsTenantValidBasedOnConstantValuesFunctionProducer tested = new IsTenantValidBasedOnConstantValuesFunctionProducer();

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private IsTenantValidBasedOnConstantValuesFunctionDefinition functionDefinition;
    private final String testFunctionName = "is_tenant_valid";
    private final String argumentType = null;
    private final String firstInvalidValue = "SAFXCVZV";
    private final String secondInvalidValue = "FGSDFGSDG";

    @Test(testName = "create the first version of the function that treats the first test value as an invalid tenant identifier")
    public void createFirstVersionOfFunction()
    {
        functionDefinition = tested.produce(new IsTenantValidBasedOnConstantValuesFunctionProducerParameters(testFunctionName, getSchema(), new HashSet<String>(singletonList(firstInvalidValue)), argumentType));
    }

    @Test(dependsOnMethods = "createFirstVersionOfFunction")
    public void functionShouldNotExistAtTheBeginning()
    {
        assertEquals(false, isFunctionExists(jdbcTemplate, testFunctionName, getSchema()));
    }

    @Test(dependsOnMethods = "functionShouldNotExistAtTheBeginning", testName = "create the first function version in the database")
    public void executeFirstVersionOfFunction()
    {
       jdbcTemplate.execute(functionDefinition.getCreateScript());
    }

    @Test(dependsOnMethods = "executeFirstVersionOfFunction")
    public void functionShouldExist()
    {
        assertEquals(true, isFunctionExists(jdbcTemplate, testFunctionName, getSchema()));
    }

    @Test(dependsOnMethods = "functionShouldExist", testName = "the first function version should return a false value for the first test value which is defined as an invalid tenant identifier")
    public void firstFunctionVersionShouldReturnFalseForFirstInvalidValue()
    {
        assertEquals(false, returnFunctionResultForValue(firstInvalidValue));
    }

    @Test(dependsOnMethods = "firstFunctionVersionShouldReturnFalseForFirstInvalidValue", testName = "the first function version should return a true value for the second test value which is not defined as an invalid tenant identifier")
    public void firstFunctionVersionShouldReturnTrueForSecondInvalidValue()
    {
        assertEquals(true, returnFunctionResultForValue(secondInvalidValue));
    }

    @Test(dependsOnMethods = "firstFunctionVersionShouldReturnFalseForFirstInvalidValue", testName = "updating the function body with the second test value as the only one invalid tenant identifier")
    public void updateFunctionBodyWithSecondValueAsOnlyOneInvalidTenantIdentifier()
    {
        functionDefinition = tested.produce(new IsTenantValidBasedOnConstantValuesFunctionProducerParameters(testFunctionName, getSchema(), new HashSet<String>(singletonList(secondInvalidValue)), argumentType));
        jdbcTemplate.execute(functionDefinition.getCreateScript());
    }

    @Test(dependsOnMethods = "updateFunctionBodyWithSecondValueAsOnlyOneInvalidTenantIdentifier", testName = "the second function version should return a false value for the second test value which is defined as an invalid tenant identifier")
    public void secondFunctionVersionShouldReturnFalseForSecondInvalidValue()
    {
        assertEquals(false, returnFunctionResultForValue(secondInvalidValue));
    }

    @Test(dependsOnMethods = "secondFunctionVersionShouldReturnFalseForSecondInvalidValue", testName = "the second function version should return a true value for the first test value which is not defined as an invalid tenant identifier")
    public void secondFunctionVersionShouldReturnTrueForFirstInvalidValue()
    {
        assertEquals(true, returnFunctionResultForValue(firstInvalidValue));
    }

    @Test(dependsOnMethods = "secondFunctionVersionShouldReturnTrueForFirstInvalidValue", alwaysRun = true)
    public void dropFunction()
    {
        jdbcTemplate.execute(functionDefinition.getDropScript());
    }

    protected abstract String getSchema();

    private boolean returnFunctionResultForValue(String value)
    {
        FunctionArgumentValue argumentValue = forString(value);
        String functionInvocation = functionDefinition.returnIsTenantValidFunctionInvocation(argumentValue);
        String selectStatement = format("SELECT %1$s;", functionInvocation);
        return selectAndReturnFirstRecordAsBoolean(jdbcTemplate, selectStatement);
    }
}
