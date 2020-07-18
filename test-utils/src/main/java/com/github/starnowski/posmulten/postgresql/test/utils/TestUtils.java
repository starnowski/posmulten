package com.github.starnowski.posmulten.postgresql.test.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtils {

    public static String VALID_CURRENT_TENANT_ID_PROPERTY_NAME = "c.c_ten";
    public static final String CLEAR_DATABASE_SCRIPT_PATH = "/com/github/starnowski/posmulten/postgresql/core/clean-database.sql";

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

    public static String selectAndReturnFirstRecordAsString(JdbcTemplate jdbcTemplate, final String sql)
    {
        return jdbcTemplate.execute(new StatementCallback<String>() {
            public String doInStatement(Statement statement) throws SQLException, DataAccessException {
                StringBuilder sb = new StringBuilder();
                sb.append(sql);
                ResultSet rs = statement.executeQuery(sb.toString());rs.next();
                return rs.getString(1);
            }
        });
    }

    public static boolean isFunctionExists(JdbcTemplate jdbcTemplate, String functionName, String schema)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT 1 FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE ");
        sb.append("pg.proname = '");
        sb.append(functionName);
        sb.append("' AND ");
        if (schema == null)
        {
            sb.append("pgn.nspname = 'public'");
        } else {
            sb.append("pgn.nspname = '");
            sb.append(schema);
            sb.append("'");
        }
        sb.append(" AND ");
        sb.append("pg.pronamespace =  pgn.oid");
        return isAnyRecordExists(jdbcTemplate, sb.toString());
    }

    public static void dropFunction(JdbcTemplate jdbcTemplate, String functionName, String schema, String... argumentsTypes)
    {
        String functionReference = returnFunctionReference(functionName, schema);
        String argumentsTypesPhrase = argumentsTypes == null ? "" : String.join(",", argumentsTypes);
        jdbcTemplate.execute("DROP FUNCTION IF EXISTS " + functionReference + "(" + argumentsTypesPhrase + ")");
    }

    public static String returnFunctionReference(String functionName, String schema)
    {
        return schema == null ? functionName : schema + "." + functionName;
    }
}
