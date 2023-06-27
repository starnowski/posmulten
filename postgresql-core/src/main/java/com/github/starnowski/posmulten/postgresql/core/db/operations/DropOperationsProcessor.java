package com.github.starnowski.posmulten.postgresql.core.db.operations;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class DropOperationsProcessor implements IDatabaseOperationsProcessor {
    @Override
    public void run(DataSource dataSource, List<SQLDefinition> sqlDefinitions) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            LinkedList<SQLDefinition> stack = new LinkedList<>();
            sqlDefinitions.forEach(stack::push);
            for (SQLDefinition sqlDefinition : stack) {
                Statement statement = connection.createStatement();
                statement.execute(sqlDefinition.getDropScript());
            }
        }
    }
}
