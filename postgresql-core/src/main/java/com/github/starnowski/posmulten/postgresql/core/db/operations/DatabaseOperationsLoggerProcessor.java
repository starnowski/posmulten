package com.github.starnowski.posmulten.postgresql.core.db.operations;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseOperationsLoggerProcessor implements IDatabaseOperationsProcessor {

    private final Logger logger = Logger.getLogger(DatabaseOperationsLoggerProcessor.class.getName());

    @Override
    public void run(DataSource dataSource, List<SQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {
        logger.info("Creation scripts");
        sqlDefinitions.forEach(definition -> logger.info(definition.getCreateScript()));
        logger.info("Drop scripts");
        sqlDefinitions.forEach(definition -> logger.info(definition.getDropScript()));
        logger.info("Validation scripts");
        sqlDefinitions.stream().flatMap(definition -> definition.getCheckingStatements().stream()).forEach(script -> logger.info(script));
    }
}
