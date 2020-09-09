package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

import com.github.starnowski.posmulten.postgresql.core.functional.tests.TestNGSpringContextWithoutGenericTransactionalSupportTests;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducerParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.Test;

import java.util.HashSet;

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isFunctionExists;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class IsTenantValidBasedOnConstantValuesFunctionDefinitionMutableTest extends TestNGSpringContextWithoutGenericTransactionalSupportTests {

    private IsTenantValidBasedOnConstantValuesFunctionProducer tested = new IsTenantValidBasedOnConstantValuesFunctionProducer();

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private IsTenantValidBasedOnConstantValuesFunctionDefinition functionDefinition;
    private final String testFunctionName = "is_tenant_valid";
    private final String argumentType = null;
    private final String firstInvalidValue = "SAFXCVZV";
    private final String secondInvalidValue = "FGSDFGSDG";

    @Test
    public void createFirstVersionOfFunction()
    {
        functionDefinition = tested.produce(new IsTenantValidBasedOnConstantValuesFunctionProducerParameters(testFunctionName, getSchema(), new HashSet<String>(singletonList(firstInvalidValue)), argumentType));
    }

    @Test(dependsOnMethods = "createFirstVersionOfFunction")
    public void functionShouldNotExistAtTheBeginning()
    {
        assertEquals(false, isFunctionExists(jdbcTemplate, testFunctionName, getSchema()));
    }

    @Test(dependsOnMethods = "functionShouldNotExistAtTheBeginning")
    public void executeFirstVersionOfFunction()
    {
       jdbcTemplate.execute(functionDefinition.getCreateScript());
    }

    @Test(dependsOnMethods = "executeFirstVersionOfFunction")
    public void functionShouldExist()
    {
        assertEquals(true, isFunctionExists(jdbcTemplate, testFunctionName, getSchema()));
    }

    //TODO Test function execution - invalid value (first)
    //TODO Test function execution - valid value (second)
    //TODO Update function body with new invalid value (second)
    //TODO Test function execution - invalid value (second)
    //TODO Test function execution - valid value (first)

    @Test(dependsOnMethods = "functionShouldExist", alwaysRun = true)
    public void dropFunction()
    {
        jdbcTemplate.execute(functionDefinition.getDropScript());
    }

    private String getSchema() {
        return null;
    }
}
