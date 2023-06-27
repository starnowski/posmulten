package com.github.starnowski.posmulten.postgresql.core.db;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.db.operations.*;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationType.*;

public class DatabaseOperationExecutor {

    private final Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap;

    public DatabaseOperationExecutor() {
        this(prepareDatabaseOperationTypeIDatabaseOperationsProcessorMap());
    }

    DatabaseOperationExecutor(Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap) {
        this.operationsProcessorMap = operationsProcessorMap;
    }

    private static Map<DatabaseOperationType, IDatabaseOperationsProcessor> prepareDatabaseOperationTypeIDatabaseOperationsProcessorMap() {
        Map<DatabaseOperationType, IDatabaseOperationsProcessor> result = new HashMap<>();
        result.put(CREATE, new CreateOperationsProcessor());
        result.put(VALIDATE, new ValidateOperationsProcessor());
        result.put(DROP, new DropOperationsProcessor());
        result.put(LOG_ALL, new DatabaseOperationsLoggerProcessor());
        return result;
    }

    public void execute(DataSource dataSource, List<SQLDefinition> sqlDefinitions, DatabaseOperationType operationType) throws SQLException, ValidationDatabaseOperationsException {
        IDatabaseOperationsProcessor processor = operationsProcessorMap.get(operationType);
        processor.run(dataSource, sqlDefinitions);
    }

    Map<DatabaseOperationType, IDatabaseOperationsProcessor> getOperationsProcessorMap() {
        return operationsProcessorMap;
    }
}