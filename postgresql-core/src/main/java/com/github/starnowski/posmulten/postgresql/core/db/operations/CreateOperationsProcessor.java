package com.github.starnowski.posmulten.postgresql.core.db.operations;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CreateOperationsProcessor implements IDatabaseOperationsProcessor {
    @Override
    public void run(DataSource dataSource, List<SQLDefinition> sqlDefinitions) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            for (SQLDefinition sqlDefinition : sqlDefinitions) {
                Statement statement = connection.createStatement();
                statement.execute(sqlDefinition.getCreateScript());
            }
        }
    }
}
