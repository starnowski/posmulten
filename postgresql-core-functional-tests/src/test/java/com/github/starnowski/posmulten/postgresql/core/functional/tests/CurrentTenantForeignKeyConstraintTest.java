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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.*;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SpringBootTest(classes = TestApplication.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CurrentTenantForeignKeyConstraintTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String CONSTRAINT_NAME = "posts_user_info_fk_cu";
    private static final String USER_TENANT = "primary_tenant";

    @Autowired
    JdbcTemplate jdbcTemplate;

    SetCurrentTenantIdFunctionDefinition setCurrentTenantIdFunctionDefinition;

    private List<SQLDefinition> sqlDefinitions = new ArrayList<>();

    @Test(description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create a constraint for a foreign key for a table that is multi-tenant aware")
    public void createSQLDefinitions()
    {
        //Create function that returns current tenant function
        GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer();
        GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition = getCurrentTenantIdFunctionProducer.produce(new GetCurrentTenantIdFunctionProducerParameters("rls_get_current_tenant", VALID_CURRENT_TENANT_ID_PROPERTY_NAME, null, null));
        sqlDefinitions.add(getCurrentTenantIdFunctionDefinition);

        //Create function that sets current tenant function
        SetCurrentTenantIdFunctionProducer setCurrentTenantIdFunctionProducer = new SetCurrentTenantIdFunctionProducer();
        setCurrentTenantIdFunctionDefinition = setCurrentTenantIdFunctionProducer.produce(new SetCurrentTenantIdFunctionProducerParameters("rls_set_current_tenant", VALID_CURRENT_TENANT_ID_PROPERTY_NAME, null, null));
        sqlDefinitions.add(setCurrentTenantIdFunctionDefinition);

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
    }

    @Test(dependsOnMethods = {"createSQLDefinitions"})
    public void constraintShouldNotExistsBeforeTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatement("public", "posts", CONSTRAINT_NAME)), "Constraint should not exists");
    }

    @Test(dependsOnMethods = {"constraintShouldNotExistsBeforeTests"})
    public void createConstraint()
    {
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

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"constraintNameShouldExistAfterCreation"})
    public void insertUserTestData()
    {
        jdbcTemplate.execute(format("INSERT INTO public.users (id, name, tenant_id) VALUES (1, 'Szymon Tarnowski', '%s');", USER_TENANT));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM users WHERE id = 1 AND name = 'Szymon Tarnowski' AND tenant_id = '%s'", USER_TENANT)), "The tests user should exists");
    }

    @Test(dependsOnMethods = {"insertUserTestData"})
    public void insertPostForUserFromSameTenant()
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM users WHERE id = 1 AND name = 'Szymon Tarnowski' AND tenant_id = '%s'", USER_TENANT)), "The tests user should exists");
        jdbcTemplate.execute(format("%s INSERT INTO posts (id, user_id, text, tenant_id) VALUES (8, 1, 'Some phrase', '%s');", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(USER_TENANT), USER_TENANT));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM posts WHERE id = 8 AND text = 'Some phrase' AND tenant_id = '%s'", USER_TENANT)), "The tests post should exists");
    }

    @Test(dependsOnMethods = {"insertUserTestData"})
    public void tryToInsertPostForUserFromDifferentTenant()
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM users WHERE id = 1 AND name = 'Szymon Tarnowski' AND tenant_id = '%s'", USER_TENANT)), "The tests user should exists");
        assertThatThrownBy(() ->
                jdbcTemplate.execute(format("%s INSERT INTO posts (id, user_id, text, tenant_id) VALUES (7, 1, 'Some phrase', '%s');", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant("Second_tenant"), USER_TENANT)))
        .isInstanceOf(DataIntegrityViolationException.class);
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM posts WHERE id = 7", "Second_tenant")), "The tests post should not exists");
    }

    @Test(dependsOnMethods = {"insertUserTestData", "insertPostForUserFromSameTenant", "tryToInsertPostForUserFromDifferentTenant"}, alwaysRun = true)
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

    @Test(dependsOnMethods = {"dropAllSQLDefinitions"}, alwaysRun = true)
    public void deleteTestData()
    {
        deleteFromTables("posts", "users");
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
