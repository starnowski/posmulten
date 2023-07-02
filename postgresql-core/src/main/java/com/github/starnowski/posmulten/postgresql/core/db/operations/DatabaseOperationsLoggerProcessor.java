/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.postgresql.core.db.operations;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseOperationsLoggerProcessor implements IDatabaseOperationsProcessor {

    private final Logger logger = Logger.getLogger(DatabaseOperationsLoggerProcessor.class.getName());

    @Override
    public void run(DataSource dataSource, List<SQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {
        this.log(sqlDefinitions);
    }

    @Override
    public void run(Connection connection, List<SQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {
        this.log(sqlDefinitions);
    }

    protected void log(List<SQLDefinition> sqlDefinitions) {
        logger.info("Creation scripts");
        sqlDefinitions.forEach(definition -> logger.info(definition.getCreateScript()));
        logger.info("Drop scripts");
        sqlDefinitions.forEach(definition -> logger.info(definition.getDropScript()));
        logger.info("Validation scripts");
        sqlDefinitions.stream().flatMap(definition -> definition.getCheckingStatements().stream()).forEach(script -> logger.info(script));
    }
}
