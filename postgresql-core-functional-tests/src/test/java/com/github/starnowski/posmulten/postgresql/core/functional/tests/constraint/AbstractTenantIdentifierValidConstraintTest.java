package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

import com.github.starnowski.posmulten.postgresql.core.functional.tests.AbstractClassWithSQLDefinitionGenerationMethods;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Comment;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Post;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsTenantIdentifierValidConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.IsTenantIdentifierValidConstraintProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducerParameters;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsTenantIdentifierValidConstraintProducerParameters.builder;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractTenantIdentifierValidConstraintTest extends AbstractClassWithSQLDefinitionGenerationMethods {


    private static final String FIRST_VALID_TENANT = "DFAXCVZ";
    private static final String SECOND_VALID_TENANT = "adfavczvc";
    private static final String FIRST_INVALID_TENANT = "abdas1dsfa";
    private static final String SECOND_INVALID_TENANT = "1234adgzcgz";
    private static final String FUNCTION_NAME = "is_tenant_id_valid";
    private static final String CONSTRAINT_NAME = "check_tenant_id_valid";

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }

    protected String getPostsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "posts";
    }

    protected String getCommentsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "comments";
    }

    @DataProvider(name = "userData")
    protected static Object[][] userData()
    {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", FIRST_VALID_TENANT), FIRST_INVALID_TENANT},
                {new User(2L, "John Doe", SECOND_VALID_TENANT), SECOND_INVALID_TENANT}
        };
    }

    @DataProvider(name = "postData")
    protected static Object[][] postData()
    {
        return new Object[][]{
                {new Post(57L, "Some phrase", 1L, FIRST_VALID_TENANT)},
                {new Post(73L, "Some text", 2L, SECOND_VALID_TENANT)}
        };
    }

    @DataProvider(name = "commentsData")
    protected static Object[][] commentsData()
    {
        return new Object[][]{
                {new Comment(113, 1L, "Comment one for primary tenant", 57L, FIRST_VALID_TENANT, null, null), FIRST_INVALID_TENANT},
                {new Comment(79, 2L, "Comment one for primary tenant", 73L, SECOND_VALID_TENANT, null, null), SECOND_INVALID_TENANT}
        };
    }

    //TODO

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create a constraint for a foreign key for a table that is multi-tenant aware")
    public void createSQLDefinitions()
    {
        //Create function that returns if tenant is valid
        IsTenantValidBasedOnConstantValuesFunctionProducer isTenantValidBasedOnConstantValuesFunctionProducer = new IsTenantValidBasedOnConstantValuesFunctionProducer();
        IsTenantValidBasedOnConstantValuesFunctionDefinition functionDefinition = isTenantValidBasedOnConstantValuesFunctionProducer.produce(new IsTenantValidBasedOnConstantValuesFunctionProducerParameters(FUNCTION_NAME, getSchema(), new HashSet<String>(Arrays.asList(FIRST_INVALID_TENANT, SECOND_INVALID_TENANT)), "VARCHAR(32)"));
        sqlDefinitions.add(functionDefinition);

        DefaultIsTenantIdentifierValidConstraintProducerParameters usersConstraintProducerParameters = builder()
                .withConstraintName(CONSTRAINT_NAME)
                .withTableName("users")
                .withTableSchema(getSchema())
                .withIIsTenantValidFunctionInvocationFactory(functionDefinition)
                .withTenantColumnName("tenant_id").build();

        DefaultIsTenantIdentifierValidConstraintProducerParameters commentsConstraintProducerParameters = builder()
                .withConstraintName(CONSTRAINT_NAME)
                .withTableName("comments")
                .withTableSchema(getSchema())
                .withIIsTenantValidFunctionInvocationFactory(functionDefinition)
                .withTenantColumnName("tenant").build();

        IsTenantIdentifierValidConstraintProducer isTenantIdentifierValidConstraintProducer = new IsTenantIdentifierValidConstraintProducer();
        sqlDefinitions.add(isTenantIdentifierValidConstraintProducer.produce(usersConstraintProducerParameters));
        sqlDefinitions.add(isTenantIdentifierValidConstraintProducer.produce(commentsConstraintProducerParameters));
    }

    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "constraint should not exists before tests execution", description = "check if constraint does not exist before executing SQL definitions")
    public void constraintShouldNotExistsBeforeTests() {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "users", CONSTRAINT_NAME)), "Constraint for users table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", CONSTRAINT_NAME)), "Constraint for comments table should not exists");
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"constraintShouldNotExistsBeforeTests"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions()
    {
        super.executeSQLDefinitions();
    }

    @Test(dependsOnMethods = {"executeSQLDefinitions"}, testName = "constraint should exist after SQL definitions executed", description = "check if constraint exist after executing SQL definitions")
    public void constraintNameShouldExistAfterCreation()
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "users", CONSTRAINT_NAME)), "Constraint for users table should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", CONSTRAINT_NAME)), "Constraint for comments table should exists");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"constraintNameShouldExistAfterCreation"}, testName = "try to insert data into to user table with invalid tenant value")
    public void tryInsertUserTestDataWintInvalidTenantValue(User user, String invalidTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        assertThatThrownBy(() ->
                jdbcTemplate.execute(format("INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), invalidTenant, getUsersTableReference()))
        )
                .isInstanceOf(DataIntegrityViolationException.class);
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryInsertUserTestDataWintInvalidTenantValue"}, testName = "insert data into to user table")
    public void insertUserTestData(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        jdbcTemplate.execute(format("INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "postData", dependsOnMethods = {"insertUserTestData"}, testName = "insert data into the post table")
    public void insertPostData(Post post)
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND tenant_id = '%2$s'", post.getUserId(), post.getTenantId(), getUsersTableReference())), "The tests user should exists");
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
        jdbcTemplate.execute(format("INSERT INTO %5$s (id, user_id, text, tenant_id) VALUES (%1$d, %2$d, '%3$s', '%4$s');", post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s'", post.getId(), post.getText(), post.getTenantId(), getPostsTableReference())), "The tests post should exists");
    }

    @Test(dataProvider = "parentCommentsData", dependsOnMethods = {"insertPostData"}, testName = "insert data into the comments table that do not have reference to the comment parent")
    public void tryToInsertCommentWithInvalidTenant(Comment comment)
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND tenant_id = '%2$s'", comment.getUserId(), comment.getTenantId(), getUsersTableReference())), "The tests user should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND tenant_id = '%2$s'", comment.getPostId(), comment.getTenantId(), getPostsTableReference())), "The tests post should exists");
        assertThat(countRowsInTableWhere(getCommentsTableReference(), "id = " + comment.getId())).isEqualTo(0);
        jdbcTemplate.execute(format("INSERT INTO %6$s (id, user_id, text, post_id, tenant) VALUES (%12$d, %2$d, '%3$s', '%4$s', '%5$s');", comment.getId(), comment.getUserId(), comment.getText(), comment.getPostId(), comment.getTenantId(), getCommentsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND text = '%2$s' AND tenant = '%3$s'", comment.getId(), comment.getText(), comment.getTenantId(), getCommentsTableReference())), "The tests comment should exists");
    }

    @Test(dataProvider = "commentsData", dependsOnMethods = {"tryToInsertCommentWithInvalidTenant"}, testName = "try to insert data into the comments table with reference to parent comment that belongs to same tenant", description = "test case assumes that constraint is not going to allow to insert data into the comments table with reference to parent comment that belongs to same tenant")
    public void insertCommentWithCorrectTenant(Object[] array)
    {
        Comment comment = (Comment) array[0];
        Comment commentWithSameTenant = (Comment) array[2];
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND user_id = %2$d", commentWithSameTenant.getId(), commentWithSameTenant.getUserId(), getCommentsTableReference())), "The tests parent comment should exists");
        assertThat(countRowsInTableWhere(getCommentsTableReference(), "id = " + comment.getId() + " AND user_id = " + comment.getUserId())).isEqualTo(0);
        jdbcTemplate.execute(format("INSERT INTO %8$s (id, user_id, text, post_id, tenant, parent_comment_id, parent_comment_user_id) VALUES (%1$d, %2$d, '%3$s', '%4$s', '%5$s', %6$d, %7$d);", comment.getId(), comment.getUserId(), comment.getText(), comment.getPostId(), comment.getTenantId(), commentWithSameTenant.getId(), commentWithSameTenant.getUserId(), getCommentsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND user_id = %3$d AND tenant = '%4$s'", comment.getId(), comment.getText(), comment.getUserId(), comment.getTenantId(), getCommentsTableReference())), "The tests comment should exists");
    }

    @Override
    @Test(dependsOnMethods = "insertCommentWithCorrectTenant", alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }

    @Test(dependsOnMethods = "dropAllSQLDefinitions", alwaysRun = true)
    public void constraintShouldNotExistsAfterTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "users", CONSTRAINT_NAME)), "Constraint for users table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", CONSTRAINT_NAME)), "Constraint for comments table should not exists");
    }
}
