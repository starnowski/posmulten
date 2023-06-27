package com.github.starnowski.posmulten.postgresql.core.db.operations;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public interface IDatabaseOperationsProcessor {

    void run(DataSource dataSource, List<SQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException;
}