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

@SpringBootTest(classes = TestApplication.class)
public class CurrentTenantForeignKeyConstraintTest extends AbstractTestNGSpringContextTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private List<SQLDefinition> sqlDefinitions;

    @Test
    public void constraintShouldNotExists()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatement("public", "posts", "posts_user_info_fk_current_tenant_con")), "Constraint should not exists");
    }

    private String createSelectStatement(String schema, String table, String constraintName)
    {
        String template = "SELECT 1\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'";
        return format(template, schema == null ? "public" : schema, table, constraintName);
    }
}
