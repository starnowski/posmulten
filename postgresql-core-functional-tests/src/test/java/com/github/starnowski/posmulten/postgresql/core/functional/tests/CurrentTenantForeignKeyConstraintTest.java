package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SpringBootTest(classes = TestApplication.class)
public class CurrentTenantForeignKeyConstraintTest extends AbstractTestNGSpringContextTests {

    private static final String CONSTRAINT_NAME = "posts_user_info_fk_current_tenant_con";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private List<SQLDefinition> sqlDefinitions;

    @Test
    public void constraintShouldNotExistsBeforeTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatement("public", "posts", CONSTRAINT_NAME)), "Constraint should not exists");
    }

    @Test(dependsOnMethods = {"constraintShouldNotExistsBeforeTests"})
    public void createConstraint()
    {
        //TODO Get current tenant function
        //TODO Does record belongs to current tenant
        //TODO Constraint
    }

    @Test(dependsOnMethods = {"createConstraint"})
    public void constraintNameShouldExistAfterCreation()
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatement("public", "posts", CONSTRAINT_NAME)), "Constraint should exists");
    }

    @Test(dependsOnMethods = {"constraintNameShouldExistAfterCreation"})
    public void dropAllSQLDefinitions()
    {
        //TODO
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
