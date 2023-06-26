package com.github.starnowski.posmulten.postgresql.core.db.operations.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtil {

    public long returnLongResultForQuery(Connection connection, String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        rs.next();
        return rs.getLong(1);
    }
}
