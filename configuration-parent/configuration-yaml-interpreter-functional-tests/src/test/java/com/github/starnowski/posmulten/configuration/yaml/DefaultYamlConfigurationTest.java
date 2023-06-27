package com.github.starnowski.posmulten.configuration.yaml;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationExecutor;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.starnowski.posmulten.configuration.yaml.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationType.*;
import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SpringBootTest(classes = TestApplication.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DefaultYamlConfigurationTest extends AbstractTransactionalTestNGSpringContextTests {

    protected static final String CORE_OWNER_USER = "postgresql-core-owner";
    protected static final String NON_PRIVILEGED_USER = "postgresql-core-user";
    protected static final String USERS_TABLE_NAME = "users";
    protected static final String POSTS_TABLE_NAME = "posts";
    protected static final String GROUPS_TABLE_NAME = "groups";
    protected static final String USERS_GROUPS_TABLE_NAME = "users_groups";
    protected static final String COMMENTS_TABLE_NAME = "comments";
    protected static final String NOTIFICATIONS_TABLE_NAME = "notifications";
    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";
    protected static final String CUSTOM_TENANT_COLUMN_NAME = "tenant";
    protected static final String POSTS_USERS_FK_CONSTRAINT_NAME = "posts_users_fk_cu";
    protected static final String COMMENTS_USERS_FK_CONSTRAINT_NAME = "comments_users_fk_cu";
    protected static final String COMMENTS_POSTS_FK_CONSTRAINT_NAME = "comments_posts_fk_cu";
    protected static final String COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME = "comments_parent_comments_fk_cu";
    protected static final String NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME = "notifications_users_fk_cu";
    protected static final String USERS_GROUPS_USERS_FK_CONSTRAINT_NAME = "users_groups_users_fk_cu";
    protected static final String USERS_GROUPS_GROUPS_FK_CONSTRAINT_NAME = "users_groups_groups_fk_cu";
    protected ISetCurrentTenantIdFunctionInvocationFactory setCurrentTenantIdFunctionInvocationFactory;
    protected ISharedSchemaContext sharedSchemaContext;
    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    @Autowired
    private DataSource dataSource;
    private DatabaseOperationExecutor databaseOperationExecutor = new DatabaseOperationExecutor();
    protected List<SQLDefinition> sqlDefinitions = new ArrayList<>();

    protected String getGranteeForSharedSchemaContextBuilderInitialization() {
        return CORE_OWNER_USER;
    }

    protected String getSchemaForSharedSchemaContextBuilderInitialization() {
        return getSchema();
    }

    protected String getSchema() {
        return "non_public_schema";
    }

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }

    @DataProvider(name = "userData")
    protected static Object[][] userData()
    {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", USER_TENANT), SECONDARY_USER_TENANT},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT), USER_TENANT}
        };
    }

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware")
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(getSchemaForSharedSchemaContextBuilderInitialization());
        defaultSharedSchemaContextBuilder.setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME);
        defaultSharedSchemaContextBuilder.setForceRowLevelSecurityForTableOwner(true);
        defaultSharedSchemaContextBuilder.setGrantee(getGranteeForSharedSchemaContextBuilderInitialization());
        defaultSharedSchemaContextBuilder.createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME);
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(GROUPS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), "tenant_id", "groups_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(USERS_GROUPS_TABLE_NAME, new HashMap<>(), "tenant_id", "users_groups_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(COMMENTS_TABLE_NAME, mapBuilder().put("id", "int").put("user_id", "bigint").build(), CUSTOM_TENANT_COLUMN_NAME, "comments_table_rls_policy");
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), POSTS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), COMMENTS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, POSTS_TABLE_NAME, mapBuilder().put("post_id", "id").build(), COMMENTS_POSTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, COMMENTS_TABLE_NAME, mapBuilder().put("parent_comment_id", "id").put("parent_comment_user_id", "user_id").build(), COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(USERS_GROUPS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), USERS_GROUPS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(USERS_GROUPS_TABLE_NAME, GROUPS_TABLE_NAME, mapBuilder().put("group_id", "uuid").build(), USERS_GROUPS_GROUPS_FK_CONSTRAINT_NAME);

        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant");
        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(POSTS_TABLE_NAME, "is_post_belongs_to_current_tenant");
        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(COMMENTS_TABLE_NAME, "is_comment_belongs_to_current_tenant");
        defaultSharedSchemaContextBuilder.setNameForFunctionThatChecksIfRecordExistsInTable(GROUPS_TABLE_NAME, "is_group_belongs_to_current_tenant");
        this.sharedSchemaContext = defaultSharedSchemaContextBuilder.build();
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
        DefaultDecoratorContext decoratorContext = DefaultDecoratorContext.builder()
                .withReplaceCharactersMap(MapBuilder.mapBuilder()
                        .put("{{template_schema_value}}", "non_public_schema")
                        .put("{{template_user_grantee}}", CORE_OWNER_USER)
                        .build())
                .build();
        SharedSchemaContextDecoratorFactory sharedSchemaContextDecoratorFactory = new SharedSchemaContextDecoratorFactory();
        sharedSchemaContext = sharedSchemaContextDecoratorFactory.build(sharedSchemaContext, decoratorContext);
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        sqlDefinitions.clear();
        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions() {
        try {
            this.databaseOperationExecutor.execute(dataSource, sharedSchemaContext.getSqlDefinitions(), CREATE);
            this.databaseOperationExecutor.execute(dataSource, sharedSchemaContext.getSqlDefinitions(), VALIDATE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ValidationDatabaseOperationsException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "try to insert data into the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to insert data into the users table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoUserTableAsDifferentTenant(User user, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant)))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToInsertDataIntoUserTableAsDifferentTenant"}, testName = "insert data into the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to the current tenant")
    public void insertDataIntoUserTableAsCurrentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"insertDataIntoUserTableAsCurrentTenant"}, testName = "try to select data from the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to select data from the users table assigned to the different tenant than currently set")
    public void tryToSelectDataFromUserTableAsDifferentTenant(User user, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        assertFalse(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE id = %1$d ) ;", user.getId(), getUsersTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant)), "The SELECT statement should not return any records for different tenant then currently set");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToSelectDataFromUserTableAsDifferentTenant"}, testName = "try to select data from the users table assigned to the current tenant", description = "test case assumes that row level security for users table is not going to allow to select data from the users table assigned to the current tenant")
    public void tryToSelectDataFromUserTableAsSameTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        assertTrue(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE id = %1$d ) ;", user.getId(), getUsersTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())), "The SELECT statement should return records for current tenant");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToSelectDataFromUserTableAsSameTenant"},  testName = "counting statement should only proceed records that belongs to current tenant", description = "test case assumes that row level security for users table will cause that the counting statement should only proceed records that belongs to current tenant")
    public void selectAllShouldReturnOnlyRecordsThatBelongsToCurrentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        int numberOfAllRecordsInUserTable = countRowsInTable(getUsersTableReference());
        Long numberOfAllRecordsInUserTableForCurrentTenant = selectAndReturnFirstRecordAsLongWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT COUNT(0) FROM %1$s;", getUsersTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId()));
        assertThat(numberOfAllRecordsInUserTableForCurrentTenant).isGreaterThan(0).isLessThan(numberOfAllRecordsInUserTable);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"selectAllShouldReturnOnlyRecordsThatBelongsToCurrentTenant"}, testName = "try to update data in the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to update data in the users table assigned to the different tenant than currently set")
    public void tryToUpdateDataInUserTableAsDifferentTenant(User user, String differentTenant)
    {
        String updatedName = "[UPDATE_NAME]" + user.getName();
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET name = '%4$s' WHERE id = %3$d ;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant), getUsersTableReference(), user.getId(), updatedName));
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(0);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToUpdateDataInUserTableAsDifferentTenant"}, testName = "update data in the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to update data in the users table assigned to the current tenant")
    public void updateDataInUserTableAsDifferentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        String updatedName = "[UPDATE_NAME]" + user.getName();
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET name = '%4$s' WHERE id = %3$d ;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId()), getUsersTableReference(), user.getId(), updatedName));
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(0);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(1);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"updateDataInUserTableAsDifferentTenant"}, testName = "try to delete data from the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to delete data from the users table assigned to the different tenant than currently set")
    public void tryToDeleteDataFromUserTableAsDifferentTenant(User user, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE id = %3$d;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant), getUsersTableReference(), user.getId()));
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToDeleteDataFromUserTableAsDifferentTenant"}, testName = "delete data from the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to delete data from the users table assigned to the current tenant")
    public void deleteDataFromUserTableAsDifferentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE id = %3$d;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId()), getUsersTableReference(), user.getId()));
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    @Test(dependsOnMethods = { "tryToInsertDataIntoUserTableAsDifferentTenant", "insertDataIntoUserTableAsCurrentTenant", "tryToSelectDataFromUserTableAsDifferentTenant", "tryToSelectDataFromUserTableAsSameTenant", "selectAllShouldReturnOnlyRecordsThatBelongsToCurrentTenant", "tryToUpdateDataInUserTableAsDifferentTenant", "updateDataInUserTableAsDifferentTenant", "tryToDeleteDataFromUserTableAsDifferentTenant", "deleteDataFromUserTableAsDifferentTenant" }, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        try {
            this.databaseOperationExecutor.execute(dataSource, sharedSchemaContext.getSqlDefinitions(), DROP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ValidationDatabaseOperationsException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType) {
        return mapBuilder().put(columnName, columnType).build();
    }
}
