package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducerParameters
import com.github.starnowski.posmulten.postgresql.core.util.SqlUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.*
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.ALL
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.DELETE
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.INSERT
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.SELECT
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.UPDATE
import static java.lang.String.format
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class RLSPolicyProducerItTest extends Specification {

    private static final String IS_TENANT_ID_CORRECT_TEST_FUNCTION = "is_tenant_starts_with_abcd"
    private static final String TENANT_HAS_AUTHORITIES_TEST_FUNCTION = "tenant_has_authorities_function"
    def tested = new RLSPolicyProducer()
    def schema
    def table
    def policyName
    SQLDefinition policyDefinition
    def tenantHasAuthoritiesFunction

    TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory1
    TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory2

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    SqlUtils sqlUtils

    def setup()
    {
        String createScript = prepareCreateScriptForFunctionThatDeterminesIfTenantIdIsCorrect()
        jdbcTemplate.execute(createScript)
        assertEquals(true, isFunctionExists(jdbcTemplate, IS_TENANT_ID_CORRECT_TEST_FUNCTION, null))
        EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory = prepareEqualsCurrentTenantIdentifierFunctionInvocationFactoryForTest()
        TenantHasAuthoritiesFunctionProducer producer = new TenantHasAuthoritiesFunctionProducer()
        tenantHasAuthoritiesFunction = producer.produce(new TenantHasAuthoritiesFunctionProducerParameters(TENANT_HAS_AUTHORITIES_TEST_FUNCTION, null, equalsCurrentTenantIdentifierFunctionInvocationFactory))
        jdbcTemplate.execute(tenantHasAuthoritiesFunction.getCreateScript())
        assertEquals(true, isFunctionExists(jdbcTemplate, TENANT_HAS_AUTHORITIES_TEST_FUNCTION, null))

        tenantHasAuthoritiesFunctionInvocationFactory1 = tenantHasAuthoritiesFunction
        tenantHasAuthoritiesFunctionInvocationFactory2 = prepareTenantHasAuthoritiesFunctionInvocationFactoryForTestFunctionThatDetermineIfTenantIdIsCorrect()
    }

    @Unroll
    def "should create policy with name '#testPolicyName' for schema '#testSchema' (null means public) and table #testTable for grantee #grantee" ()
    {
        given:
            policyName = testPolicyName
            schema = testSchema
            table = testTable
            assertEquals(false, isRLSPolicyExists(jdbcTemplate, policyName, table, schema))
            assertEquals(false, isRLSPolicyForGranteeExists(jdbcTemplate, policyName, table, schema, grantee))

        when:
            policyDefinition = tested.produce(builder().withPolicyName(policyName)
                                                        .withPolicySchema(schema)
                                                        .withPolicyTable(table)
                                                        .withGrantee(grantee)
                                                        .withPermissionCommandPolicy(ALL)
                                                        .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                                                        .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory2)
                                                        .build())
            sqlUtils.assertAllResultForCheckingStatementsAreEqualZero(policyDefinition)
            jdbcTemplate.execute(policyDefinition.getCreateScript())

        then:
            isRLSPolicyExists(jdbcTemplate, policyName, table, schema)
            isRLSPolicyForGranteeExists(jdbcTemplate, policyName, table, schema, grantee)
            sqlUtils.assertAllCheckingStatementsArePassing(policyDefinition)

        where:
            testSchema              |   testPolicyName          |   testTable       |   grantee
            null                    |   "users_policy"          |   "users"         |   "postgresql-core-user"
            "public"                |   "users_policy"          |   "users"         |   "postgresql-core-user"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "postgresql-core-user"
            null                    |   "users_policy"          |   "users"         |   "postgresql-core-owner"
            "public"                |   "users_policy"          |   "users"         |   "postgresql-core-owner"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "postgresql-core-owner"
            null                    |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"
            "public"                |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"
            "non_public_schema"     |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"
            null                    |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner"
            "public"                |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner"
            "non_public_schema"     |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner"
    }

    @Unroll
    def "should create policy with name '#testPolicyName' for schema '#testSchema' (null means public) and table #testTable for permission command #permissionCommand" ()
    {
        given:
            policyName = testPolicyName
            schema = testSchema
            table = testTable
            assertEquals(false, isRLSPolicyExists(jdbcTemplate, policyName, table, schema))
            assertEquals(false, isRLSPolicyForPermissionCommandPolicyExists(jdbcTemplate, policyName, table, schema, permissionCommand))

        when:
            policyDefinition = tested.produce(builder().withPolicyName(policyName)
                    .withPolicySchema(schema)
                    .withPolicyTable(table)
                    .withGrantee("postgresql-core-user")
                    .withPermissionCommandPolicy(permissionCommand)
                    .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory2)
                    .build())
            jdbcTemplate.execute(policyDefinition.getCreateScript())

        then:
            isRLSPolicyExists(jdbcTemplate, policyName, table, schema)
            isRLSPolicyForPermissionCommandPolicyExists(jdbcTemplate, policyName, table, schema, permissionCommand)

        where:
            testSchema              |   testPolicyName          |   testTable       |   permissionCommand
            null                    |   "users_policy"          |   "users"         |   ALL
            "public"                |   "users_policy"          |   "users"         |   SELECT
            "non_public_schema"     |   "users_policy"          |   "users"         |   INSERT
            null                    |   "users_policy"          |   "users"         |   UPDATE
            "public"                |   "users_policy"          |   "users"         |   DELETE
            null                    |   "users_groups_policy"   |   "users_groups"  |   ALL
            "public"                |   "users_groups_policy"   |   "users_groups"  |   SELECT
            "non_public_schema"     |   "users_groups_policy"   |   "users_groups"  |   INSERT
            null                    |   "users_groups_policy"   |   "users_groups"  |   UPDATE
            "public"                |   "users_groups_policy"   |   "users_groups"  |   DELETE
    }

    def cleanup()
    {
        // Drop policy
        jdbcTemplate.execute(policyDefinition.getDropScript())
        assertEquals(false, isRLSPolicyExists(jdbcTemplate, policyName, table, schema))

        // Drop functions
        jdbcTemplate.execute(tenantHasAuthoritiesFunction.getDropScript())
        assertEquals(false, isFunctionExists(jdbcTemplate, TENANT_HAS_AUTHORITIES_TEST_FUNCTION, null))
        dropFunction(jdbcTemplate, IS_TENANT_ID_CORRECT_TEST_FUNCTION, null, "text")
        assertEquals(false, isFunctionExists(jdbcTemplate, IS_TENANT_ID_CORRECT_TEST_FUNCTION, null))
    }

    private boolean isRLSPolicyExists(JdbcTemplate jdbcTemplate, String policy, String table, String schema)
    {
        return isAnyRecordExists(jdbcTemplate, prepareStatementThatSelectPolicyExists(policy, table, schema))
    }

    private boolean isRLSPolicyForGranteeExists(JdbcTemplate jdbcTemplate, String policy, String table, String schema, String grantee)
    {
        return isAnyRecordExists(jdbcTemplate, prepareStatementThatSelectPolicyForGranteeExists(policy, table, schema, grantee))
    }

    private boolean isRLSPolicyForPermissionCommandPolicyExists(JdbcTemplate jdbcTemplate, String policy, String table, String schema, PermissionCommandPolicyEnum permissionCommandPolicy)
    {
        return isAnyRecordExists(jdbcTemplate, prepareStatementThatSelectPolicyWithSpecifiedCmdExists(policy, table, schema, permissionCommandPolicy))
    }

    private String prepareCreateScriptForFunctionThatDeterminesIfTenantIdIsCorrect() {
        StringBuilder sb = new StringBuilder()
        sb.append("CREATE OR REPLACE FUNCTION ")
        sb.append(IS_TENANT_ID_CORRECT_TEST_FUNCTION)
        sb.append("(text) RETURNS BOOLEAN AS \$\$")
        sb.append("\n")
        sb.append("SELECT \$1 LIKE 'ABCD%'")
        sb.append("\n")
        sb.append("\$\$ LANGUAGE sql;")
        sb.toString()
    }

    private Closure<String> prepareEqualsCurrentTenantIdentifierFunctionInvocationFactoryForTest() {
        { tenant ->
            IS_TENANT_ID_CORRECT_TEST_FUNCTION + "(" + mapToString(tenant) + ")"
        }
    }

    private Closure<String> prepareTenantHasAuthoritiesFunctionInvocationFactoryForTestFunctionThatDetermineIfTenantIdIsCorrect() {
        { tenantIdValue, permissionCommandPolicy, rlsExpressionType, table, schema ->
            IS_TENANT_ID_CORRECT_TEST_FUNCTION + "(" + mapToString(tenantIdValue) + ")"
        }
    }

    def prepareStatementThatSelectPolicyExists(String name, String table, String schema)
    {
        def schemaName = schema == null ? "public" : schema
        format("SELECT pg.polname, pg.polcmd, pc.relname, pn.nspname FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = '%1\$s' AND pc.relname = '%2\$s' AND pn.nspname = '%3\$s'", name, table, schemaName)
    }

    def prepareStatementThatSelectPolicyWithSpecifiedCmdExists(String name, String table, String schema, PermissionCommandPolicyEnum permissionCommandPolicy)
    {
        def schemaName = schema == null ? "public" : schema
        def cmd
        switch (permissionCommandPolicy)
        {
            case ALL:
                cmd = "*"
                break
            case SELECT:
                cmd = "r"
                break
            case INSERT:
                cmd = "a"
                break
            case UPDATE:
                cmd = "w"
                break
            case DELETE:
                cmd = "d"
        }
        format("SELECT pg.polname, pg.polcmd, pc.relname, pn.nspname FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = '%1\$s' AND pc.relname = '%2\$s' AND pn.nspname = '%3\$s' AND pg.polcmd = '%4\$s'", name, table, schemaName, cmd)
    }

    def prepareStatementThatSelectPolicyForGranteeExists(String name, String table, String schema, String grantee)
    {
        def schemaName = schema == null ? "public" : schema
        format("SELECT pg.polname, pg.polcmd, pc.relname, pn.nspname, ro.rolname FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn, pg_roles ro WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = '%1\$s' AND pc.relname = '%2\$s' AND pn.nspname = '%3\$s' AND ro.oid = ANY(pg.polroles) AND ro.rolname = '%4\$s'", name, table, schemaName, grantee)
    }
}
