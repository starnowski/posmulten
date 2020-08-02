package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Comment;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Post;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractCreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableTest extends AbstractClassWithSQLDefinitionGenerationMethods {

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";

    abstract  protected String getSchema();

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
                {new User(1L, "Szymon Tarnowski", USER_TENANT)},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT)}
        };
    }

    @DataProvider(name = "postData")
    protected static Object[][] postData()
    {
        return new Object[][]{
                {new Post(57L, "Some phrase", 1L, USER_TENANT)},
                {new Post(73L, "Some text", 2L, SECONDARY_USER_TENANT)}
        };
    }

    @DataProvider(name = "parentCommentsData")
    protected static Object[][] parentCommentsData()
    {
        return new Object[][]{
                {new Comment(113, 1L, "Comment one for primary tenant", 57L, USER_TENANT, null, null)},
                {new Comment(79, 2L, "Comment one for primary tenant", 73L, SECONDARY_USER_TENANT, null, null)}
        };
    }

    /**
     * Produce data with comment pojo, comment identifier that belongs to different tenant, comment identifier that belongs to same tenant
     * @return
     */
    @DataProvider(name = "commentsWithParentsData")
    protected static Object[][] commentsWithParentsData()
    {
        return new Object[][] {
                {new Comment(209, 1L, "Comment one for primary tenant - child", 57L, USER_TENANT, null, null), new Comment().setId(79).setUserId(2L), new Comment().setId(113).setUserId(1L)},
                {new Comment(67, 2L, "Comment one for primary tenant - child", 73L, SECONDARY_USER_TENANT, null, null), new Comment().setId(113).setUserId(1L), new Comment().setId(79).setUserId(2L)}
        };
    }

    SetCurrentTenantIdFunctionDefinition setCurrentTenantIdFunctionDefinition;

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create a constraint for a foreign key for a table that is multi-tenant aware")
    public void createSQLDefinitions()
    {
        //Create function that returns current tenant function
        GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer();
        GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition = getCurrentTenantIdFunctionProducer.produce(new GetCurrentTenantIdFunctionProducerParameters("rls_get_current_tenant", VALID_CURRENT_TENANT_ID_PROPERTY_NAME, getSchema(), null));
        sqlDefinitions.add(getCurrentTenantIdFunctionDefinition);

        //Create function that sets current tenant function
        SetCurrentTenantIdFunctionProducer setCurrentTenantIdFunctionProducer = new SetCurrentTenantIdFunctionProducer();
        setCurrentTenantIdFunctionDefinition = setCurrentTenantIdFunctionProducer.produce(new SetCurrentTenantIdFunctionProducerParameters("rls_set_current_tenant", VALID_CURRENT_TENANT_ID_PROPERTY_NAME, getSchema(), null));
        sqlDefinitions.add(setCurrentTenantIdFunctionDefinition);

        // Does record belongs to current tenant (users table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isUsersRecordBelongsToCurrentTenantFunctionDefinition = getIsUsersRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isUsersRecordBelongsToCurrentTenantFunctionDefinition);

        // Does record belongs to current tenant (posts table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isPostsRecordBelongsToCurrentTenantFunctionDefinition = getIsPostsRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isPostsRecordBelongsToCurrentTenantFunctionDefinition);

        // Does record belongs to current tenant (comments table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isCommentsRecordBelongsToCurrentTenantFunctionDefinition = getIsCommentsRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isCommentsRecordBelongsToCurrentTenantFunctionDefinition);

        // Constraint - post - fk - users
        //user_id
        SQLDefinition recordBelongsToCurrentTenantConstrainSqlDefinition = getSqlDefinitionOfConstraintForUsersForeignKeyInPostsTable(isUsersRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(recordBelongsToCurrentTenantConstrainSqlDefinition);

        //getSqlDefinitionOfConstraintForUsersForeignKeyInCommentsTable
        SQLDefinition usersBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition = getSqlDefinitionOfConstraintForUsersForeignKeyInCommentsTable(isUsersRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(usersBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition);

        //getSqlDefinitionOfConstraintForPostsForeignKeyInCommentsTable commets - posts fk
        SQLDefinition postsBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition = getSqlDefinitionOfConstraintForPostsForeignKeyInCommentsTable(isPostsRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(postsBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition);

        //getSqlDefinitionOfConstraintForParentCommentForeignKeyInCommentsTable comments - parent comment fk
        SQLDefinition parentCommentBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition = getSqlDefinitionOfConstraintForParentCommentForeignKeyInCommentsTable(isCommentsRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(parentCommentBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition);
    }

    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "constraint should not exists before tests execution", description = "check if constraint does not exist before executing SQL definitions")
    public void constraintShouldNotExistsBeforeTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "posts", POSTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in posts table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in comments table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_POSTS_FK_CONSTRAINT_NAME)), "Constraint for posts for foreign key in comments table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME)), "Constraint for parent comment for foreign key in comments table should not exists");
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"constraintShouldNotExistsBeforeTests"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions()
    {
        sqlDefinitions.forEach(sqlDefinition ->
        {
            jdbcTemplate.execute(sqlDefinition.getCreateScript());
        });
    }

    @Test(dependsOnMethods = {"executeSQLDefinitions"}, testName = "constraint should exist after SQL definitions executed", description = "check if constraint exist after executing SQL definitions")
    public void constraintNameShouldExistAfterCreation()
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "posts", POSTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in posts table should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in comments table should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_POSTS_FK_CONSTRAINT_NAME)), "Constraint for posts for foreign key in comments table should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME)), "Constraint for parent comment for foreign key in comments table should exists");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"constraintNameShouldExistAfterCreation"}, testName = "insert data into to user table")
    public void insertUserTestData(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        jdbcTemplate.execute(format("INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "postData", dependsOnMethods = {"insertUserTestData"}, testName = "insert data into the post table")
    public void insertPostForUserFromSameTenant(Post post)
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND tenant_id = '%2$s'", post.getUserId(), post.getTenantId(), getUsersTableReference())), "The tests user should exists");
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
        jdbcTemplate.execute(format("%1$s INSERT INTO %6$s (id, user_id, text, tenant_id) VALUES (%2$d, %3$d, '%4$s', '%5$s');", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(post.getTenantId()), post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s'", post.getId(), post.getText(), post.getTenantId(), getPostsTableReference())), "The tests post should exists");
    }

    @Test(dataProvider = "parentCommentsData", dependsOnMethods = {"insertPostForUserFromSameTenant"}, testName = "insert data into the comments table that do not have reference to the comment parent")
    public void insertCommentsWithoutParentCommentReference(Comment comment)
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND tenant_id = '%2$s'", comment.getUserId(), comment.getTenantId(), getUsersTableReference())), "The tests user should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND tenant_id = '%2$s'", comment.getPostId(), comment.getTenantId(), getPostsTableReference())), "The tests post should exists");
        assertThat(countRowsInTableWhere(getCommentsTableReference(), "id = " + comment.getId())).isEqualTo(0);
        jdbcTemplate.execute(format("%1$s INSERT INTO %7$s (id, user_id, text, post_id, tenant) VALUES (%2$d, %3$d, '%4$s', '%5$s', '%6$s');", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(comment.getTenantId()), comment.getId(), comment.getUserId(), comment.getText(), comment.getPostId(), comment.getTenantId(), getCommentsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND text = '%2$s' AND tenant = '%3$s'", comment.getId(), comment.getText(), comment.getTenantId(), getCommentsTableReference())), "The tests comment should exists");
    }

    @Test(dataProvider = "commentsWithParentsData", dependsOnMethods = {"insertCommentsWithoutParentCommentReference"}, testName = "try to insert data into the comments table with reference to parent comment that belongs to different tenant", description = "test case assumes that constraint is not going to allow to insert data into the comments table with reference to parent comment that belongs to different tenant")
    public void tryToInsertCommentWithParentCommentWhenParentBelongsToDifferentTenant(Object[] array)
    {
        Comment comment = (Comment) array[0];
        Comment differentTenantComment = (Comment) array[1];
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND user_id = %2$d", differentTenantComment.getId(), differentTenantComment.getUserId(), getCommentsTableReference())), "The tests parent comment should exists");
        assertThat(countRowsInTableWhere(getCommentsTableReference(), "id = " + comment.getId() + " AND user_id = " + comment.getUserId())).isEqualTo(0);
        assertThatThrownBy(() ->
                jdbcTemplate.execute(format("%1$s INSERT INTO %9$s (id, user_id, text, post_id, tenant, parent_comment_id, parent_comment_user_id) VALUES (%2$d, %3$d, '%4$s', '%5$s', '%6$s', %7$d, %8$d);", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(comment.getTenantId()), comment.getId(), comment.getUserId(), comment.getText(), comment.getPostId(), comment.getTenantId(), differentTenantComment.getId(), differentTenantComment.getUserId(), getCommentsTableReference()))
        )
                .isInstanceOf(DataIntegrityViolationException.class);
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND user_id = %3$d AND tenant = '%4$s'", comment.getId(), comment.getText(), comment.getUserId(), comment.getTenantId(), getCommentsTableReference())), "The tests comment should not exists");
    }

    @Test(dataProvider = "commentsWithParentsData", dependsOnMethods = {"tryToInsertCommentWithParentCommentWhenParentBelongsToDifferentTenant"}, testName = "try to insert data into the comments table with reference to parent comment that belongs to same tenant", description = "test case assumes that constraint is not going to allow to insert data into the comments table with reference to parent comment that belongs to same tenant")
    public void insertCommentWithParentCommentWhenParentBelongsToSameTenant(Object[] array)
    {
        Comment comment = (Comment) array[0];
        Comment commentWithSameTenant = (Comment) array[2];
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND user_id = %2$d", commentWithSameTenant.getId(), commentWithSameTenant.getUserId(), getCommentsTableReference())), "The tests parent comment should exists");
        assertThat(countRowsInTableWhere(getCommentsTableReference(), "id = " + comment.getId() + " AND user_id = " + comment.getUserId())).isEqualTo(0);
        jdbcTemplate.execute(format("%1$s INSERT INTO %9$s (id, user_id, text, post_id, tenant, parent_comment_id, parent_comment_user_id) VALUES (%2$d, %3$d, '%4$s', '%5$s', '%6$s', %7$d, %8$d);", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(comment.getTenantId()), comment.getId(), comment.getUserId(), comment.getText(), comment.getPostId(), comment.getTenantId(), commentWithSameTenant.getId(), commentWithSameTenant.getUserId(), getCommentsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND user_id = %3$d AND tenant = '%4$s'", comment.getId(), comment.getText(), comment.getUserId(), comment.getTenantId(), getCommentsTableReference())), "The tests comment should exists");
    }

    @Test(dependsOnMethods = {"insertCommentsWithoutParentCommentReference", "tryToInsertCommentWithParentCommentWhenParentBelongsToDifferentTenant", "insertCommentWithParentCommentWhenParentBelongsToSameTenant"}, alwaysRun = true)
    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    public void deleteTestData()
    {
        assertThat(countRowsInTable(getUsersTableReference())).isEqualTo(0);
        assertThat(countRowsInTable(getPostsTableReference())).isEqualTo(0);
        assertThat(countRowsInTable(getCommentsTableReference())).isEqualTo(0);
    }

    @AfterClass(dependsOnMethods = "dropAllSQLDefinitions", alwaysRun = true)
    public void constraintShouldNotExistsAfterTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "posts", POSTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in posts table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in comments table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_POSTS_FK_CONSTRAINT_NAME)), "Constraint for posts for foreign key in comments table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME)), "Constraint for parent comment for foreign key in comments table should not exists");
    }
}
