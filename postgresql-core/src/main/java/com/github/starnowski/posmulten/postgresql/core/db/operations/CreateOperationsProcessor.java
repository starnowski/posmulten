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
package com.github.starnowski.posmulten.postgresql.core.db.operations;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Database operation process that executes creation scripts.
 */
public class CreateOperationsProcessor implements IDatabaseOperationsProcessor {
    /**
     * Executes creation scripts for dataSource object.
     * Process attempts to establish a connection with the data source that this DataSource object represents.
     * At the end of operation the established connection object is going to be closed.
     * @param dataSource Datasource object
     * @param sqlDefinitions list of sql definitions objects
     * @throws SQLException
     * @throws ValidationDatabaseOperationsException
     */
    @Override
    public void run(DataSource dataSource, List<SQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {
        try (Connection connection = dataSource.getConnection()) {
            this.run(connection, sqlDefinitions);
        }
    }

    /**
     * Executes creation scripts for passed connection object.
     * @param connection Connection object
     * @param sqlDefinitions list of sql definitions objects
     * @throws SQLException
     * @throws ValidationDatabaseOperationsException
     */
    @Override
    public void run(Connection connection, List<SQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {
        for (SQLDefinition sqlDefinition : sqlDefinitions) {
            Statement statement = connection.createStatement();
            statement.execute(sqlDefinition.getCreateScript());
        }
    }
}
