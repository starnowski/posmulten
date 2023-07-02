/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posmulten.postgresql.core.db;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.db.operations.*;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional.ofNullable(operationsProcessorMap.get(operationType)).orElseThrow(() -> new IllegalArgumentException("DatabaseOperationType can not be null")).run(dataSource, sqlDefinitions);
    }

    public void execute(Connection connection, List<SQLDefinition> sqlDefinitions, DatabaseOperationType operationType) throws SQLException, ValidationDatabaseOperationsException {
        Optional.ofNullable(operationsProcessorMap.get(operationType)).orElseThrow(() -> new IllegalArgumentException("DatabaseOperationType can not be null")).run(connection, sqlDefinitions);
    }

    Map<DatabaseOperationType, IDatabaseOperationsProcessor> getOperationsProcessorMap() {
        return operationsProcessorMap;
    }
}