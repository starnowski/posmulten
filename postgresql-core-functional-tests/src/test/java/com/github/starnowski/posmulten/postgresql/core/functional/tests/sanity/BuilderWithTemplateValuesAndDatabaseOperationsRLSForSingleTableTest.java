package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationExecutor;
import com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationType;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import java.sql.SQLException;

import static com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationType.*;

public class BuilderWithTemplateValuesAndDatabaseOperationsRLSForSingleTableTest extends BuilderWithTemplateValuesRLSForSingleTableTest {

    @Autowired
    private DataSource dataSource;
    private DatabaseOperationExecutor databaseOperationExecutor = new DatabaseOperationExecutor();

    @Override
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

    @Override
    public void dropAllSQLDefinitions() {
        try {
            this.databaseOperationExecutor.execute(dataSource, sharedSchemaContext.getSqlDefinitions(), DROP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ValidationDatabaseOperationsException e) {
            throw new RuntimeException(e);
        }
    }
}
