package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.*;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SpringBootTest(classes = TestApplication.class)
public class CurrentTenantForeignKeyConstraintTest extends AbstractTestNGSpringContextTests {

    private static final String CONSTRAINT_NAME = "posts_user_info_fk_cu";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private List<SQLDefinition> sqlDefinitions = new ArrayList<>();

    @Test
    public void constraintShouldNotExistsBeforeTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatement("public", "posts", CONSTRAINT_NAME)), "Constraint should not exists");
    }

    @Test(dependsOnMethods = {"constraintShouldNotExistsBeforeTests"})
    public void createConstraint()
    {
        //Create function that returns current tenant function
        GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer();
        GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition = getCurrentTenantIdFunctionProducer.produce(new GetCurrentTenantIdFunctionProducerParameters("rls_get_current_tenant", VALID_CURRENT_TENANT_ID_PROPERTY_NAME, null, null));
        sqlDefinitions.add(getCurrentTenantIdFunctionDefinition);

        // Does record belongs to current tenant
        AbstractIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(null)
                .withFunctionName("is_user_belongs_to_current_tenant")
                .withRecordTableName("users")
                .withRecordSchemaName(null)
                .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionDefinition)
                .withTenantColumn("tenant_id")
                .withKeyColumnsPairsList(ImmutableList.of(pairOfColumnWithType("id", "bigint"))).build();
        IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();
        IsRecordBelongsToCurrentTenantFunctionDefinition isRecordBelongsToCurrentTenantFunctionDefinition = isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
        sqlDefinitions.add(isRecordBelongsToCurrentTenantFunctionDefinition);

        // Constraint
        IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer = new IsRecordBelongsToCurrentTenantConstraintProducer();
        //user_id
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("user_id"));
        IsRecordBelongsToCurrentTenantConstraintProducerParameters isRecordBelongsToCurrentTenantConstraintProducerParameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(CONSTRAINT_NAME)
                .withTableName("posts")
                .withTableSchema(null)
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionDefinition)
                .withPrimaryColumnsValuesMap(primaryColumnsValuesMap).build();
        SQLDefinition recordBelongsToCurrentTenantConstrainSqlDefinition = isRecordBelongsToCurrentTenantConstraintProducer.produce(isRecordBelongsToCurrentTenantConstraintProducerParameters);
        sqlDefinitions.add(recordBelongsToCurrentTenantConstrainSqlDefinition);

        sqlDefinitions.forEach(sqlDefinition ->
        {
            jdbcTemplate.execute(sqlDefinition.getCreateScript());
        });
    }

    @Test(dependsOnMethods = {"createConstraint"})
    public void constraintNameShouldExistAfterCreation()
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatement("public", "posts", CONSTRAINT_NAME)), "Constraint should exists");
    }

    @Test(dependsOnMethods = {"constraintNameShouldExistAfterCreation"}, alwaysRun = true)
    public void dropAllSQLDefinitions()
    {
        //Run sql statements in reverse order
        LinkedList<SQLDefinition> stack = new LinkedList<>();
        sqlDefinitions.forEach(stack::push);
        stack.forEach(sqlDefinition ->
        {
            jdbcTemplate.execute(sqlDefinition.getDropScript());
        });
    }

    @Test(dependsOnMethods = {"dropAllSQLDefinitions"})
    public void constraintShouldNotExistsAfterTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatement("public", "posts", CONSTRAINT_NAME)), "Constraint should not exists");
    }

    private String createSelectStatement(String schema, String table, String constraintName)
    {
        String template = "SELECT 1\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'";
        return format(template, schema == null ? "public" : schema, table, constraintName);
    }
}
