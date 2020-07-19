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

@SpringBootTest(classes = TestApplication.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TestNGSpringContextWithoutGenericTransactionalSupportTests extends AbstractTransactionalTestNGSpringContextTests {

    protected List<SQLDefinition> sqlDefinitions = new ArrayList<>();

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
