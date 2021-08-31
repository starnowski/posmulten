package com.github.starnowski.posmulten.postgresql.test.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class TestUtils {

    public static String VALID_CURRENT_TENANT_ID_PROPERTY_NAME = "c.c_ten";
    public static final String CLEAR_DATABASE_SCRIPT_PATH = "/com/github/starnowski/posmulten/postgresql/core/clean-database.sql";

    public static boolean isAnyRecordExists(JdbcTemplate jdbcTemplate, final String sql) {
        return jdbcTemplate.execute((StatementCallback<Boolean>) statement -> {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT EXISTS ( ");
            sb.append(sql);
            sb.append(")");
            ResultSet rs = statement.executeQuery(sb.toString());
            rs.next();
            return rs.getBoolean(1);
        });
    }

    public static String selectAndReturnFirstRecordAsString(JdbcTemplate jdbcTemplate, final String sql) {
        return jdbcTemplate.execute((StatementCallback<String>) statement -> {
            StringBuilder sb = new StringBuilder();
            sb.append(sql);
            ResultSet rs = statement.executeQuery(sb.toString());
            rs.next();
            return rs.getString(1);
        });
    }

    public static Boolean selectAndReturnFirstRecordAsBoolean(JdbcTemplate jdbcTemplate, final String sql) {
        return jdbcTemplate.execute((StatementCallback<Boolean>) statement -> {
            StringBuilder sb = new StringBuilder();
            sb.append(sql);
            ResultSet rs = statement.executeQuery(sb.toString());
            rs.next();
            return rs.getBoolean(1);
        });
    }

    public static boolean isFunctionExists(JdbcTemplate jdbcTemplate, String functionName, String schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT 1 FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE ");
        sb.append("pg.proname = '");
        sb.append(functionName);
        sb.append("' AND ");
        if (schema == null) {
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

    public static boolean selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(JdbcTemplate jdbcTemplate, String selectStatement, String setCurrentTenantIdStatement) {
        return jdbcTemplate.execute((StatementCallback<Boolean>) statement -> {
            statement.execute(setCurrentTenantIdStatement);
            ResultSet rs = statement.executeQuery(selectStatement);
            rs.next();
            return rs.getBoolean(1);
        });
    }

    public static Long selectAndReturnFirstRecordAsLongWithSettingCurrentTenantId(JdbcTemplate jdbcTemplate, String selectStatement, String setCurrentTenantIdStatement) {
        return jdbcTemplate.execute((StatementCallback<Long>) statement -> {
            statement.execute(setCurrentTenantIdStatement);
            ResultSet rs = statement.executeQuery(selectStatement);
            rs.next();
            return rs.getLong(1);
        });
    }

    public static Map<String, Long> selectAndReturnMapOfStatementsAndItResultsForListOfSelectStatements(JdbcTemplate jdbcTemplate, List<String> selectStatements) {
        return selectStatements.stream().map(selectStatement -> {
                    Long result = jdbcTemplate.execute((StatementCallback<Long>) statement -> {
                        ResultSet rs = statement.executeQuery(selectStatement);
                        rs.next();
                        return rs.getLong(1);
                    });
                    return new StatementAndItLongResult(selectStatement, result);
                }
        ).collect(Collectors.toMap(sr -> sr.statement, sr -> sr.result));
    }

    public static void dropFunction(JdbcTemplate jdbcTemplate, String functionName, String schema, String... argumentsTypes) {
        String functionReference = returnFunctionReference(functionName, schema);
        String argumentsTypesPhrase = argumentsTypes == null ? "" : String.join(",", argumentsTypes);
        jdbcTemplate.execute("DROP FUNCTION IF EXISTS " + functionReference + "(" + argumentsTypesPhrase + ")");
    }

    public static String returnFunctionReference(String functionName, String schema) {
        return schema == null ? functionName : schema + "." + functionName;
    }

    public static boolean isConstraintExists(JdbcTemplate jdbcTemplate, String schema, String table, String constraintName) {
        String template = "SELECT 1\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'";
        String selectStatement = format(template, schema == null ? "public" : schema, table, constraintName);
        return isAnyRecordExists(jdbcTemplate, selectStatement);
    }

    private static class StatementAndItLongResult {
        public StatementAndItLongResult(String statement, Long result) {
            this.statement = statement;
            this.result = result;
        }

        private final String statement;
        private final Long result;
    }
}
