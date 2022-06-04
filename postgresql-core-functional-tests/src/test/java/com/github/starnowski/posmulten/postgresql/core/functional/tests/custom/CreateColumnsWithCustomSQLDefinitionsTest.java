package com.github.starnowski.posmulten.postgresql.core.functional.tests.custom;

import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.DefaultTestNGTest;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertTrue;

public class CreateColumnsWithCustomSQLDefinitionsTest extends DefaultTestNGTest {

    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    @Test(testName = "create SQL definitions", description = "Create shared schema with custom sql definitions that creates two custom columns for table 'user'")
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        ISharedSchemaContext result = (new DefaultSharedSchemaContextBuilder(null))
                .setGrantee(CORE_OWNER_USER)
                .addCustomSQLDefinition(CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING, "ALTER TABLE users ADD COLUMN custom_column1 VARCHAR(255);", "ALTER TABLE users DROP COLUMN custom_column1;",
                        singletonList("SELECT COUNT(1) FROM information_schema.columns WHERE table_catalog = 'postgresql_core' AND table_schema = 'public' AND table_name = 'users' AND column_name = 'custom_column1';"))
                .addCustomSQLDefinition(CustomSQLDefinitionPairDefaultPosition.AT_END, "ALTER TABLE users ADD COLUMN custom_column2 VARCHAR(255);", "ALTER TABLE users DROP COLUMN custom_column2;",
                        singletonList("SELECT COUNT(1) FROM information_schema.columns WHERE table_catalog = 'postgresql_core' AND table_schema = 'public' AND table_name = 'users' AND column_name = 'custom_column2';"))
                .build();

        sqlDefinitions.addAll(result.getSqlDefinitions());
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions() {
        super.executeSQLDefinitions();
    }

    @Test(dependsOnMethods = {"executeSQLDefinitions"}, testName = "insert data into to user table with values for new custom columns")
    public void insertUserTestData() {
        User user = new User(1L, "name1", "tenan1");
        assertThat(countRowsInTableWhere("users", "id = " + user.getId())).isEqualTo(0);
        jdbcTemplate.execute(format("INSERT INTO %4$s (id, name, tenant_id, custom_column1, custom_column2) VALUES (%1$d, '%2$s', '%3$s', '%5$s', '%6$s');", user.getId(), user.getName(), user.getTenantId(), "users", "x1", "x2"));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND custom_column1 = '%2$s' AND custom_column2 = '%3$s'", user.getId(), "x1", "x2", "users")), "The tests user should exists");
    }

    @Override
    @Test(dependsOnMethods = {"insertUserTestData"}, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }
}
