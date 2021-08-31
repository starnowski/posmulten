package com.github.starnowski.posmulten.postgresql.test.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

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

    public static Boolean selectAndReturnFirstRecordAsBoolean(JdbcTemplate jdbcTemplate, final String sql)
    {
        return jdbcTemplate.execute(new StatementCallback<Boolean>() {
            public Boolean doInStatement(Statement statement) throws SQLException, DataAccessException {
                StringBuilder sb = new StringBuilder();
                sb.append(sql);
                ResultSet rs = statement.executeQuery(sb.toString());rs.next();
                return rs.getBoolean(1);
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

    public static boolean  selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(JdbcTemplate jdbcTemplate, String selectStatement, String setCurrentTenantIdStatement)
    {
        return jdbcTemplate.execute(new StatementCallback<Boolean>() {
            @Override
            public Boolean doInStatement(Statement statement) throws SQLException, DataAccessException {
                statement.execute(setCurrentTenantIdStatement);
                ResultSet rs = statement.executeQuery(selectStatement);
                rs.next();
                return rs.getBoolean(1);
            }
        });
    }

    public static Long  selectAndReturnFirstRecordAsLongWithSettingCurrentTenantId(JdbcTemplate jdbcTemplate, String selectStatement, String setCurrentTenantIdStatement)
    {
        return jdbcTemplate.execute(new StatementCallback<Long>() {
            @Override
            public Long doInStatement(Statement statement) throws SQLException, DataAccessException {
                statement.execute(setCurrentTenantIdStatement);
                ResultSet rs = statement.executeQuery(selectStatement);
                rs.next();
                return rs.getLong(1);
            }
        });
    }

    public static List<Long> selectAndReturnListOfLongObjectForListOfSelectStatements(JdbcTemplate jdbcTemplate, List<String> selectStatements)
    {
        return selectStatements.stream().map(selectStatement -> jdbcTemplate.execute(new StatementCallback<Long>() {
            @Override
            public Long doInStatement(Statement statement) throws SQLException, DataAccessException {
                ResultSet rs = statement.executeQuery(selectStatement);
                rs.next();
                return rs.getLong(1);
            }
        })
        ).collect(toList());
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

    public static boolean isConstraintExists(JdbcTemplate jdbcTemplate, String schema, String table, String constraintName)
    {
        String template = "SELECT 1\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'";
        String selectStatement = format(template, schema == null ? "public" : schema, table, constraintName);
        return isAnyRecordExists(jdbcTemplate, selectStatement);
    }
}
