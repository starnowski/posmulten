package com.github.starnowski.posmulten.postgresql.core.functional.tests.combine;

import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Post;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractRLSWithSettingReferenceWithConstraintForCurrentTenantForeignKeyTest extends FullStackTest{

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";

    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }

    protected String getPostsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "posts";
    }

    @DataProvider(name = "userData")
    protected static Object[][] userData()
    {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", USER_TENANT)},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT)}
        };
    }

    @DataProvider(name = "postWithUserReferenceFromOtherTenantData")
    protected static Object[][] postWithUserReferenceFromOtherTenantData()
    {
        return new Object[][]{
                {new Post(8L, "Some phrase", 2L, USER_TENANT)},
                {new Post(13L, "Some text", 1L, SECONDARY_USER_TENANT)}
        };
    }

    @DataProvider(name = "postWithUserReferenceForSameTenantData")
    protected static Object[][] postWithUserReferenceForSameTenantData()
    {
        return new Object[][]{
                {new Post(79L, "Some phrase", 1L, USER_TENANT), 2L},
                {new Post(197L, "Some text", 2L, SECONDARY_USER_TENANT), 1L}
        };
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions()
    {
        super.executeSQLDefinitions();
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "insert data into to user table")
    public void insertUserTestData(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "postWithUserReferenceFromOtherTenantData", dependsOnMethods = {"insertUserTestData"}, testName = "insert data into the post table with reference to users table that belongs to different tenant")
    public void insertPostWithReferenceToUserFromDifferentTenant(Post post)
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d", post.getUserId(), getUsersTableReference())), "The tests user should exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", post.getUserId(), getUsersTableReference(), post.getTenantId())), "The tests user should not belong to same tenant as posts record");
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s INSERT INTO %6$s (id, user_id, text, tenant_id) VALUES (%2$d, %3$d, '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s' AND user_id = %4$d", post.getId(), post.getText(), post.getTenantId(), post.getUserId(), getPostsTableReference())), "The tests post should exists");
    }

    @Test(dataProvider = "postWithUserReferenceForSameTenantData", dependsOnMethods = {"insertPostWithReferenceToUserFromDifferentTenant"}, testName = "insert data into the post table with reference to users table that belongs to same tenant")
    public void insertPostWithReferenceToUserFromSameTenant(Object[] parameters)
    {
        Post post = (Post) parameters[0];
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d", post.getUserId(), getUsersTableReference())), "The tests user should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", post.getUserId(), getUsersTableReference(), post.getTenantId())), "The tests user should belong to same tenant as posts record");
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s INSERT INTO %6$s (id, user_id, text, tenant_id) VALUES (%2$d, %3$d, '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s' AND user_id = %4$d", post.getId(), post.getText(), post.getTenantId(), post.getUserId(), getPostsTableReference())), "The tests post should exists");
    }

    @Test(dataProvider = "postWithUserReferenceForSameTenantData", dependsOnMethods = {"insertPostWithReferenceToUserFromSameTenant"}, testName = "update data in the post table with reference to users table that belongs to different tenant")
    public void updatePostWithReferenceToUserFromDifferentTenant(Object[] parameters)
    {
        Post post = (Post) parameters[0];
        Long userIdForDifferentTenant = (Long) parameters[1];
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d", userIdForDifferentTenant, getUsersTableReference())), "The tests user should exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", userIdForDifferentTenant, getUsersTableReference(), post.getTenantId())), "The tests user should not belong to same tenant as posts record");
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET user_id = %4$d WHERE id = %3$d;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), getPostsTableReference(), post.getId(), userIdForDifferentTenant));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s' AND user_id = %4$d", post.getId(), post.getText(), post.getTenantId(), userIdForDifferentTenant, getPostsTableReference())), "The tests post should exists with reference to user that belongs to different tenant");
    }

    @Test(dependsOnMethods = {"insertPostWithReferenceToUserFromDifferentTenant", "insertPostWithReferenceToUserFromSameTenant", "updatePostWithReferenceToUserFromDifferentTenant"}, alwaysRun = true)
    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    public void deleteTestData()
    {
        assertThat(countRowsInTable(getUsersTableReference())).isEqualTo(0);
        assertThat(countRowsInTable(getPostsTableReference())).isEqualTo(0);
    }

    @Override
    @Test(dependsOnMethods = "deleteTestData", alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }
}
