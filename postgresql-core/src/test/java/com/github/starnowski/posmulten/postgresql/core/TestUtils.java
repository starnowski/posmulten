package com.github.starnowski.posmulten.postgresql.core;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtils {

    public static boolean isAnyRecordExists(JdbcTemplate jdbcTemplate, final String sql)
    {
        return jdbcTemplate.execute(new StatementCallback<Boolean>() {
            public Boolean doInStatement(Statement statement) throws SQLException, DataAccessException {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT EXISTS ( ");
                sb.append(sql);
                sb.append(")");
                ResultSet rs = statement.executeQuery(sb.toString());rs.next();
                return rs.getBoolean(1);
            }
        });
    }
}
