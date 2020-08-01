package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Post;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableTest extends AbstractClassWithSQLDefinitionGenerationMethods {
    //TODO

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";

    protected String getSchema()
    {
        return null;
    }

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

    @DataProvider(name = "postData")
    protected static Object[][] postData()
    {
        return new Object[][]{
                {new AbstractCreateCurrentTenantForeignKeyConstraintForPostsTableTest.PostData(new Post(8L, "Some phrase", 1L, USER_TENANT), USER_TENANT, SECONDARY_USER_TENANT, 2L)},
                {new AbstractCreateCurrentTenantForeignKeyConstraintForPostsTableTest.PostData(new Post(13L, "Some text", 2L, SECONDARY_USER_TENANT), SECONDARY_USER_TENANT, USER_TENANT, 1L)}
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

    @AfterClass(dependsOnMethods = "dropAllSQLDefinitions", alwaysRun = true)
    public void constraintShouldNotExistsAfterTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "posts", POSTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in posts table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_USERS_FK_CONSTRAINT_NAME)), "Constraint for users for foreign key in comments table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_POSTS_FK_CONSTRAINT_NAME)), "Constraint for posts for foreign key in comments table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), "comments", COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME)), "Constraint for parent comment for foreign key in comments table should not exists");
    }
}
