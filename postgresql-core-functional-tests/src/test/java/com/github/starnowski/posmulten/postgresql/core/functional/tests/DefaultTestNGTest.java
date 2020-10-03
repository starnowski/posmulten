package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.lang.String.format;

public class DefaultTestNGTest extends TestNGSpringContextWithoutGenericTransactionalSupportTests{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final String CORE_OWNER_USER = "postgresql-core-owner";
    protected static final String NON_PRIVILEGED_USER = "postgresql-core-user";
    protected static final String USERS_TABLE_NAME = "users";
    protected static final String POSTS_TABLE_NAME = "posts";
    protected static final String GROUPS_TABLE_NAME = "groups";
    protected static final String USERS_GROUPS_TABLE_NAME = "users_groups";
    protected static final String COMMENTS_TABLE_NAME = "comments";
    protected static final String NOTIFICATIONS_TABLE_NAME = "notifications";

    protected List<SQLDefinition> sqlDefinitions = new ArrayList<>();

    protected String createSelectStatementForConstraintName(String schema, String table, String constraintName)
    {
        String template = "SELECT 1\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s'";
        return format(template, schema == null ? "public" : schema, table, constraintName);
    }

    public void executeSQLDefinitions()
    {
        sqlDefinitions.forEach(sqlDefinition ->
        {
            log.info("Executing creation script: " + sqlDefinition.getCreateScript());
            jdbcTemplate.execute(sqlDefinition.getCreateScript());
        });
    }

    //    @AfterTest(alwaysRun = true, inheritGroups = false)
//    @AfterClass(alwaysRun = true)
    public void dropAllSQLDefinitions()
    {
        if (sqlDefinitions != null) {
            //Run sql statements in reverse order
            LinkedList<SQLDefinition> stack = new LinkedList<>();
            sqlDefinitions.forEach(stack::push);
            stack.forEach(sqlDefinition ->
            {
                log.info("Executing drop script: " + sqlDefinition.getDropScript());
                jdbcTemplate.execute(sqlDefinition.getDropScript());
            });
        }
    }

    protected static class MapBuilder <K, V>
    {
        private Map<K, V> map = new HashMap<>();

        public MapBuilder put(K key, V value)
        {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build()
        {
            return map;
        }
    }

    protected static <K, V> MapBuilder<K, V> mapBuilder()
    {
        return new MapBuilder<>();
    }
}
