package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected List<SQLDefinition> sqlDefinitions = new ArrayList<>();

    protected String createSelectStatementForConstraintName(String schema, String table, String constraintName)
    {
        String template = "SELECT 1\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'";
        return format(template, schema == null ? "public" : schema, table, constraintName);
    }

    public void executeSQLDefinitions()
    {
        sqlDefinitions.forEach(sqlDefinition ->
        {
            log.info("Executing creation script: " + sqlDefinition.getDropScript());
            jdbcTemplate.execute(sqlDefinition.getCreateScript());
        });
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
                log.info("Executing drop script: " + sqlDefinition.getDropScript());
                jdbcTemplate.execute(sqlDefinition.getDropScript());
            });
        }
    }
}
