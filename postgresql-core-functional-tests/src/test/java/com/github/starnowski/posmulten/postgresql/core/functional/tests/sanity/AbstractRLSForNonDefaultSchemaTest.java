package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.AbstractClassWithSQLDefinitionGenerationMethods;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Post;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractRLSForNonDefaultSchemaTest extends AbstractClassWithSQLDefinitionGenerationMethods {

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";
    protected static final String CUSTOM_TENANT_COLUMN_NAME = "tenant";
    protected ISetCurrentTenantIdFunctionInvocationFactory setCurrentTenantIdFunctionInvocationFactory;
    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    @DataProvider(name = "userData")
    protected static Object[][] userData() {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", USER_TENANT), null},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT), null},
                {new User(1L, "Szymon Tarnowski", USER_TENANT), "non_public_schema"},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT), "non_public_schema"},
                {new User(11L, "Szymon Tarnowski", USER_TENANT), null},
                {new User(12L, "John Doe", SECONDARY_USER_TENANT), null},
                {new User(21L, "Szymon Tarnowski", USER_TENANT), "non_public_schema"},
                {new User(22L, "John Doe", SECONDARY_USER_TENANT), "non_public_schema"}
        };
    }

    @DataProvider(name = "postWithUserReferenceFromOtherTenantData")
    protected static Object[][] postWithUserReferenceFromOtherTenantData() {
        return new Object[][]{
                {new Post(8L, "Some phrase", 2L, USER_TENANT), null},
                {new Post(13L, "Some text", 1L, SECONDARY_USER_TENANT), null},
                {new Post(8L, "Some phrase", 2L, USER_TENANT), "non_public_schema"},
                {new Post(13L, "Some text", 1L, SECONDARY_USER_TENANT), "non_public_schema"}
        };
    }

    @DataProvider(name = "postWithUserReferenceFromSameTenantButInDifferentSchemaData")
    protected static Object[][] postWithUserReferenceFromSameTenantButInDifferentSchemaData() {
        return new Object[][]{
                {new Post(108L, "Some phrase", 22L, SECONDARY_USER_TENANT), null, "non_public_schema"},
                {new Post(113L, "Some text", 21L, USER_TENANT), null, "non_public_schema"},
                {new Post(108L, "Some phrase", 12L, SECONDARY_USER_TENANT), "non_public_schema", null},
                {new Post(113L, "Some text", 11L, USER_TENANT), "non_public_schema", null}
        };
    }

    @DataProvider(name = "postWithUserReferenceForSameTenantData")
    protected static Object[][] postWithUserReferenceForSameTenantData() {
        return new Object[][]{
                {new Post(79L, "Some phrase", 1L, USER_TENANT), 2L, null},
                {new Post(197L, "Some text", 2L, SECONDARY_USER_TENANT), 1L, null},
                {new Post(79L, "Some phrase", 1L, USER_TENANT), 2L, "non_public_schema"},
                {new Post(197L, "Some text", 2L, SECONDARY_USER_TENANT), 1L, "non_public_schema"}
        };
    }

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware")
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(getSchema());
        defaultSharedSchemaContextBuilder.setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME);
        defaultSharedSchemaContextBuilder.setForceRowLevelSecurityForTableOwner(true);
        defaultSharedSchemaContextBuilder.setGrantee(CORE_OWNER_USER);
        appendDeclarationForSchema(defaultSharedSchemaContextBuilder, null);
        appendDeclarationForSchema(defaultSharedSchemaContextBuilder, "non_public_schema");
        ISharedSchemaContext sharedSchemaContext = defaultSharedSchemaContextBuilder.build();
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
    }

    private void appendDeclarationForSchema(DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder, String schema) {
        defaultSharedSchemaContextBuilder.createTenantColumnForTable(new TableKey(NOTIFICATIONS_TABLE_NAME, schema));
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(new TableKey(NOTIFICATIONS_TABLE_NAME, schema), prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(new TableKey(USERS_TABLE_NAME, schema), prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(new TableKey(POSTS_TABLE_NAME, schema), prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(new TableKey(GROUPS_TABLE_NAME, schema), prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), "tenant_id", "groups_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(new TableKey(USERS_GROUPS_TABLE_NAME, schema), new HashMap<>(), "tenant_id", "users_groups_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(new TableKey(COMMENTS_TABLE_NAME, schema), mapBuilder().put("id", "int").put("user_id", "bigint").build(), CUSTOM_TENANT_COLUMN_NAME, "comments_table_rls_policy");
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(new TableKey(POSTS_TABLE_NAME, schema), new TableKey(USERS_TABLE_NAME, schema), mapBuilder().put("user_id", "id").build(), POSTS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(new TableKey(COMMENTS_TABLE_NAME, schema), new TableKey(USERS_TABLE_NAME, schema), mapBuilder().put("user_id", "id").build(), COMMENTS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(new TableKey(COMMENTS_TABLE_NAME, schema), new TableKey(POSTS_TABLE_NAME, schema), mapBuilder().put("post_id", "id").build(), COMMENTS_POSTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(new TableKey(COMMENTS_TABLE_NAME, schema), new TableKey(COMMENTS_TABLE_NAME, schema), mapBuilder().put("parent_comment_id", "id").put("parent_comment_user_id", "user_id").build(), COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(new TableKey(NOTIFICATIONS_TABLE_NAME, schema), new TableKey(USERS_TABLE_NAME, schema), mapBuilder().put("user_id", "id").build(), NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(new TableKey(USERS_GROUPS_TABLE_NAME, schema), new TableKey(USERS_TABLE_NAME, schema), mapBuilder().put("user_id", "id").build(), USERS_GROUPS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(new TableKey(USERS_GROUPS_TABLE_NAME, schema), new TableKey(GROUPS_TABLE_NAME, schema), mapBuilder().put("group_id", "uuid").build(), USERS_GROUPS_GROUPS_FK_CONSTRAINT_NAME);

        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey(USERS_TABLE_NAME, schema), "is_user_belongs_to_current_tenant");
        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey(POSTS_TABLE_NAME, schema), "is_post_belongs_to_current_tenant");
        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey(COMMENTS_TABLE_NAME, schema), "is_comment_belongs_to_current_tenant");
        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey(GROUPS_TABLE_NAME, schema), "is_group_belongs_to_current_tenant");
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType) {
        return mapBuilder().put(columnName, columnType).build();
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions() {
        super.executeSQLDefinitions();
    }

    private String getUsersTableReference(String schema) {
        return (schema == null ? "" : schema + ".") + "users";
    }

    private String getPostsTableReference(String schema) {
        return (schema == null ? "" : schema + ".") + "posts";
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "insert data into to user table")
    public void insertUserTestData(User user, String schema) {
        assertThat(countRowsInTableWhere(getUsersTableReference(schema), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(schema), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(schema))), "The tests user should exists");
    }

    @Test(dataProvider = "postWithUserReferenceFromOtherTenantData", dependsOnMethods = {"insertUserTestData"}, testName = "try to insert data into the post table with reference to users table that belongs to different tenant")
    public void tryInsertPostWithReferenceToUserFromDifferentTenant(Post post, String schema) {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d", post.getUserId(), getUsersTableReference(schema))), "The tests user should exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", post.getUserId(), getUsersTableReference(schema), post.getTenantId())), "The tests user should not belong to same tenant as posts record");
        assertThat(countRowsInTableWhere(getPostsTableReference(schema), "id = " + post.getUserId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%1$s INSERT INTO %6$s (id, user_id, text, tenant_id) VALUES (%2$d, %3$d, '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference(schema)))
        )
                .isInstanceOf(DataIntegrityViolationException.class);
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s' AND user_id = %4$d", post.getId(), post.getText(), post.getTenantId(), post.getUserId(), getPostsTableReference(schema))), "The tests post should not exists");
    }

    @Test(dataProvider = "postWithUserReferenceFromSameTenantButInDifferentSchemaData", dependsOnMethods = {"insertUserTestData"}, testName = "try to insert data into the post table with reference to users table that belongs to different tenant")
    public void tryInsertPostWithReferenceToUserFromSameTenantButInDifferentSchema(Post post, String schema, String differentSchema) {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", post.getUserId(), getUsersTableReference(differentSchema), post.getTenantId())), "The tests user should exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", post.getUserId(), getUsersTableReference(schema), post.getTenantId())), "The tests user should belong to same tenant as posts record but in different schema");
        assertThat(countRowsInTableWhere(getPostsTableReference(schema), "id = " + post.getUserId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%1$s INSERT INTO %6$s (id, user_id, text, tenant_id) VALUES (%2$d, %3$d, '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference(schema)))
        )
                .isInstanceOf(DataIntegrityViolationException.class);
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s' AND user_id = %4$d", post.getId(), post.getText(), post.getTenantId(), post.getUserId(), getPostsTableReference(schema))), "The tests post should not exists");
    }

    @Test(dataProvider = "postWithUserReferenceForSameTenantData", dependsOnMethods = {"tryInsertPostWithReferenceToUserFromDifferentTenant", "tryInsertPostWithReferenceToUserFromSameTenantButInDifferentSchema"}, testName = "insert data into the post table with reference to users table that belongs to same tenant")
    public void insertPostWithReferenceToUserFromSameTenant(Post post, Long userId, String schema) {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d", post.getUserId(), getUsersTableReference(schema))), "The tests user should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", post.getUserId(), getUsersTableReference(schema), post.getTenantId())), "The tests user should belong to same tenant as posts record");
        assertThat(countRowsInTableWhere(getPostsTableReference(schema), "id = " + post.getUserId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s INSERT INTO %6$s (id, user_id, text, tenant_id) VALUES (%2$d, %3$d, '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference(schema)));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s' AND user_id = %4$d", post.getId(), post.getText(), post.getTenantId(), post.getUserId(), getPostsTableReference(schema))), "The tests post should exists");
    }

    @Test(dataProvider = "postWithUserReferenceForSameTenantData", dependsOnMethods = {"insertPostWithReferenceToUserFromSameTenant"}, testName = "try to update data in the post table with reference to users table that belongs to different tenant")
    public void tryUpdatePostWithReferenceToUserFromDifferentTenant(Post post, Long userIdForDifferentTenant, String schema) {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d", userIdForDifferentTenant, getUsersTableReference(schema))), "The tests user should exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %2$s WHERE id = %1$d AND tenant_id = '%3$s'", userIdForDifferentTenant, getUsersTableReference(schema), post.getTenantId())), "The tests user should not belong to same tenant as posts record");
        assertThat(countRowsInTableWhere(getPostsTableReference(schema), "id = " + post.getUserId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET user_id = %4$d WHERE id = %3$d;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), getPostsTableReference(schema), post.getId(), userIdForDifferentTenant))
        )
                .isInstanceOf(DataIntegrityViolationException.class);
        assertFalse(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %5$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s' AND user_id = %4$d", post.getId(), post.getText(), post.getTenantId(), userIdForDifferentTenant, getPostsTableReference(schema))), "The tests post should not exists with reference to user that belongs to different tenant");
    }

    @Test(dependsOnMethods = {"tryInsertPostWithReferenceToUserFromDifferentTenant", "insertPostWithReferenceToUserFromSameTenant", "tryUpdatePostWithReferenceToUserFromDifferentTenant", "tryInsertPostWithReferenceToUserFromSameTenantButInDifferentSchema"}, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }

    @Test(dependsOnMethods = {"dropAllSQLDefinitions"}, alwaysRun = true)
    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    public void deleteTestData() {
        assertThat(countRowsInTable(getUsersTableReference(null))).isEqualTo(0);
        assertThat(countRowsInTable(getUsersTableReference("non_public_schema"))).isEqualTo(0);
        assertThat(countRowsInTable(getPostsTableReference(null))).isEqualTo(0);
        assertThat(countRowsInTable(getPostsTableReference("non_public_schema"))).isEqualTo(0);
    }
}
