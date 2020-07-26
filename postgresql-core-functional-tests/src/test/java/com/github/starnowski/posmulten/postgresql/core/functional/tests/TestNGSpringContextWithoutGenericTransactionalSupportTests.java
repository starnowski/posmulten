package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

@SpringBootTest(classes = TestApplication.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TestNGSpringContextWithoutGenericTransactionalSupportTests extends AbstractTransactionalTestNGSpringContextTests {

    protected List<SQLDefinition> sqlDefinitions = new ArrayList<>();

    protected String createSelectStatementForConstraintName(String schema, String table, String constraintName)
    {
        String template = "SELECT 1\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'";
        return format(template, schema == null ? "public" : schema, table, constraintName);
    }

//    @AfterTest(alwaysRun = true, inheritGroups = false)
    @AfterClass(alwaysRun = true)
    public void dropAllSQLDefinitions()
    {
        if (sqlDefinitions != null) {
            //Run sql statements in reverse order
            LinkedList<SQLDefinition> stack = new LinkedList<>();
            sqlDefinitions.forEach(stack::push);
            stack.forEach(sqlDefinition ->
            {
                jdbcTemplate.execute(sqlDefinition.getDropScript());
            });
        }
    }
}
