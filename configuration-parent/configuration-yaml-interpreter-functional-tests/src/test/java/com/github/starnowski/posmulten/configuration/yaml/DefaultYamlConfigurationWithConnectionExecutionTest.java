package com.github.starnowski.posmulten.configuration.yaml;

import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static com.github.starnowski.posmulten.configuration.yaml.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationType.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest(classes = TestApplication.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DefaultYamlConfigurationWithConnectionExecutionTest extends DefaultYamlConfigurationTest {

    @DataProvider(name = "userData")
    protected static Object[][] userData() {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", USER_TENANT), SECONDARY_USER_TENANT},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT), USER_TENANT}
        };
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            this.databaseOperationExecutor.execute(connection, sharedSchemaContext.getSqlDefinitions(), CREATE);
            this.databaseOperationExecutor.execute(connection, sharedSchemaContext.getSqlDefinitions(), LOG_ALL);
            this.databaseOperationExecutor.execute(connection, sharedSchemaContext.getSqlDefinitions(), VALIDATE);
        } catch (ValidationDatabaseOperationsException e) {
            throw new RuntimeException(e);
        }
    }


    @Test(dependsOnMethods = {"tryToInsertDataIntoUserTableAsDifferentTenant", "insertDataIntoUserTableAsCurrentTenant", "tryToSelectDataFromUserTableAsDifferentTenant", "tryToSelectDataFromUserTableAsSameTenant", "selectAllShouldReturnOnlyRecordsThatBelongsToCurrentTenant", "tryToUpdateDataInUserTableAsDifferentTenant", "updateDataInUserTableAsDifferentTenant", "tryToDeleteDataFromUserTableAsDifferentTenant", "deleteDataFromUserTableAsDifferentTenant"}, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        try (Connection connection = dataSource.getConnection()) {
            this.databaseOperationExecutor.execute(connection, sharedSchemaContext.getSqlDefinitions(), DROP);
        } catch (ValidationDatabaseOperationsException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
