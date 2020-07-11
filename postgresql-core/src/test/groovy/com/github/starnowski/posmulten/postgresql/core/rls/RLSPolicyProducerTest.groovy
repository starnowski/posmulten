package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.test.utils.RandomString
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducerParameters
import spock.lang.Specification
import spock.lang.Unroll

import java.util.logging.Logger

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.*
import static java.lang.String.format

class RLSPolicyProducerTest extends Specification {

    private static final String IS_TENANT_ID_CORRECT_TEST_FUNCTION = "is_tenant_starts_with_abcd"
    private static final String TENANT_HAS_AUTHORITIES_TEST_FUNCTION = "tenant_has_authorities_function"
    private static final Logger logger = Logger.getLogger(RLSPolicyProducerTest.class.getName())

    TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory1
    TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory2
    def tested = new RLSPolicyProducer()

    def setup()
    {
        EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory = prepareEqualsCurrentTenantIdentifierFunctionInvocationFactoryForTest()
        TenantHasAuthoritiesFunctionProducer producer = new TenantHasAuthoritiesFunctionProducer()
        def tenantHasAuthoritiesFunction = producer.produce(new TenantHasAuthoritiesFunctionProducerParameters(TENANT_HAS_AUTHORITIES_TEST_FUNCTION, null, equalsCurrentTenantIdentifierFunctionInvocationFactory))

        tenantHasAuthoritiesFunctionInvocationFactory1 = tenantHasAuthoritiesFunction
        tenantHasAuthoritiesFunctionInvocationFactory2 = prepareTenantHasAuthoritiesFunctionInvocationFactoryForTestFunctionThatDetermineIfTenantIdIsCorrect()
    }

    @Unroll
    def "for policy name '#policyName' for schema '#schema' and table #table for grantee #grantee and permission command #permissionCommand should create statement : #expectedStatement" ()
    {
        expect:
            tested.produce(builder().withPolicyName(policyName)
                .withPolicySchema(schema)
                .withPolicyTable(table)
                .withGrantee(grantee)
                .withPermissionCommandPolicy(permissionCommand)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                .build()).getCreateScript() == expectedStatement

        where:
            schema                  |   policyName              |   table           |   grantee                 |   permissionCommand   ||  expectedStatement
            null                    |   "users_policy"          |   "users"         |   "postgresql-core-user"  |   ALL                 || "CREATE POLICY users_policy ON users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'ALL', 'USING', 'users', 'public'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'public'));"
            "public"                |   "users_policy"          |   "users"         |   "postgresql-core-user"  |   INSERT              || "CREATE POLICY users_policy ON public.users\nFOR INSERT\nTO \"postgresql-core-user\"\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'INSERT', 'WITH_CHECK', 'users', 'public'));"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "postgresql-core-user"  |   SELECT              || "CREATE POLICY users_policy ON non_public_schema.users\nFOR SELECT\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'SELECT', 'USING', 'users', 'non_public_schema'));"
            null                    |   "users_policy"          |   "users"         |   "postgresql-core-owner" |   UPDATE              || "CREATE POLICY users_policy ON users\nFOR UPDATE\nTO \"postgresql-core-owner\"\nUSING (tenant_has_authorities_function(tenant_id, 'UPDATE', 'USING', 'users', 'public'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'UPDATE', 'WITH_CHECK', 'users', 'public'));"
            "public"                |   "users_policy"          |   "users"         |   "postgresql-core-owner" |   DELETE              || "CREATE POLICY users_policy ON public.users\nFOR DELETE\nTO \"postgresql-core-owner\"\nUSING (tenant_has_authorities_function(tenant_id, 'DELETE', 'USING', 'users', 'public'));"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "postgresql-core-owner" |   ALL                 || "CREATE POLICY users_policy ON non_public_schema.users\nFOR ALL\nTO \"postgresql-core-owner\"\nUSING (tenant_has_authorities_function(tenant_id, 'ALL', 'USING', 'users', 'non_public_schema'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'non_public_schema'));"
            null                    |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"  |   INSERT              || "CREATE POLICY users_groups_policy ON users_groups\nFOR INSERT\nTO \"postgresql-core-user\"\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'INSERT', 'WITH_CHECK', 'users_groups', 'public'));"
            "public"                |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"  |   SELECT              || "CREATE POLICY users_groups_policy ON public.users_groups\nFOR SELECT\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'SELECT', 'USING', 'users_groups', 'public'));"
            "non_public_schema"     |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"  |   UPDATE              || "CREATE POLICY users_groups_policy ON non_public_schema.users_groups\nFOR UPDATE\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'UPDATE', 'USING', 'users_groups', 'non_public_schema'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'UPDATE', 'WITH_CHECK', 'users_groups', 'non_public_schema'));"
            null                    |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner" |   DELETE              || "CREATE POLICY users_groups_policy ON users_groups\nFOR DELETE\nTO \"postgresql-core-owner\"\nUSING (tenant_has_authorities_function(tenant_id, 'DELETE', 'USING', 'users_groups', 'public'));"
            "public"                |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner" |   ALL                 || "CREATE POLICY users_groups_policy ON public.users_groups\nFOR ALL\nTO \"postgresql-core-owner\"\nUSING (tenant_has_authorities_function(tenant_id, 'ALL', 'USING', 'users_groups', 'public'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'ALL', 'WITH_CHECK', 'users_groups', 'public'));"
            "non_public_schema"     |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner" |   INSERT              || "CREATE POLICY users_groups_policy ON non_public_schema.users_groups\nFOR INSERT\nTO \"postgresql-core-owner\"\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'INSERT', 'WITH_CHECK', 'users_groups', 'non_public_schema'));"
    }

    @Unroll
    def "for policy name '#policyName' for schema '#schema' and table #table and tenant id column #tenantIdColumn should create statement : #expectedStatement" ()
    {
        expect:
            tested.produce(builder().withPolicyName(policyName)
                    .withPolicySchema(schema)
                    .withPolicyTable(table)
                    .withTenantIdColumn(tenantIdColumn)
                    .withGrantee("postgresql-core-user")
                    .withPermissionCommandPolicy(ALL)
                    .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .build()).getCreateScript() == expectedStatement

        where:
            schema                  |   policyName              |   table           |   tenantIdColumn      ||  expectedStatement
            null                    |   "users_policy"          |   "users"         |   "tenant"            || "CREATE POLICY users_policy ON users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant, 'ALL', 'USING', 'users', 'public'))\nWITH CHECK (tenant_has_authorities_function(tenant, 'ALL', 'WITH_CHECK', 'users', 'public'));"
            "public"                |   "users_policy"          |   "users"         |   "tenant"            || "CREATE POLICY users_policy ON public.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant, 'ALL', 'USING', 'users', 'public'))\nWITH CHECK (tenant_has_authorities_function(tenant, 'ALL', 'WITH_CHECK', 'users', 'public'));"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "tenant"            || "CREATE POLICY users_policy ON non_public_schema.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant, 'ALL', 'USING', 'users', 'non_public_schema'))\nWITH CHECK (tenant_has_authorities_function(tenant, 'ALL', 'WITH_CHECK', 'users', 'non_public_schema'));"
            null                    |   "users_policy"          |   "users"         |   "ti"                || "CREATE POLICY users_policy ON users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(ti, 'ALL', 'USING', 'users', 'public'))\nWITH CHECK (tenant_has_authorities_function(ti, 'ALL', 'WITH_CHECK', 'users', 'public'));"
            "public"                |   "users_policy"          |   "users"         |   "ti"                || "CREATE POLICY users_policy ON public.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(ti, 'ALL', 'USING', 'users', 'public'))\nWITH CHECK (tenant_has_authorities_function(ti, 'ALL', 'WITH_CHECK', 'users', 'public'));"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "ti"                || "CREATE POLICY users_policy ON non_public_schema.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(ti, 'ALL', 'USING', 'users', 'non_public_schema'))\nWITH CHECK (tenant_has_authorities_function(ti, 'ALL', 'WITH_CHECK', 'users', 'non_public_schema'));"
    }

    @Unroll
    def "for the USING expression factory #usingExpressionFactory and the CHECK WITH expression factory #checkWithExpressionFactory  should create statement : #expectedStatement" ()
    {
        expect:
            tested.produce(builder().withPolicyName("users_policy")
                    .withPolicySchema("public")
                    .withPolicyTable("users")
                    .withGrantee("postgresql-core-user")
                    .withPermissionCommandPolicy(ALL)
                    .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(getProperty(usingExpressionFactory))
                    .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(getProperty(checkWithExpressionFactory))
                    .build()).getCreateScript() == expectedStatement

        where:
            usingExpressionFactory                              |   checkWithExpressionFactory                          ||  expectedStatement
            "tenantHasAuthoritiesFunctionInvocationFactory1"    |   "tenantHasAuthoritiesFunctionInvocationFactory2"    ||  "CREATE POLICY users_policy ON public.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'ALL', 'USING', 'users', 'public'))\nWITH CHECK (is_tenant_starts_with_abcd(tenant_id));"
            "tenantHasAuthoritiesFunctionInvocationFactory2"    |   "tenantHasAuthoritiesFunctionInvocationFactory2"    ||  "CREATE POLICY users_policy ON public.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (is_tenant_starts_with_abcd(tenant_id))\nWITH CHECK (is_tenant_starts_with_abcd(tenant_id));"
            "tenantHasAuthoritiesFunctionInvocationFactory2"    |   "tenantHasAuthoritiesFunctionInvocationFactory1"    ||  "CREATE POLICY users_policy ON public.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (is_tenant_starts_with_abcd(tenant_id))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'public'));"
            "tenantHasAuthoritiesFunctionInvocationFactory1"    |   "tenantHasAuthoritiesFunctionInvocationFactory1"    ||  "CREATE POLICY users_policy ON public.users\nFOR ALL\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'ALL', 'USING', 'users', 'public'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'ALL', 'WITH_CHECK', 'users', 'public'));"
    }

    @Unroll
    def "for policy name '#policyName' for schema '#schema' and table #table should create correct drop script : #expectedStatement" ()
    {
        expect:
            tested.produce(builder().withPolicyName(policyName)
                    .withPolicySchema(schema)
                    .withPolicyTable(table)
                    .withGrantee("some_user")
                    .withPermissionCommandPolicy(ALL)
                    .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .build()).getDropScript() == expectedStatement

        where:
            schema                  |   policyName              |   table           ||  expectedStatement
            null                    |   "users_policy"          |   "users"         || "DROP POLICY IF EXISTS users_policy ON users"
            "public"                |   "users_policy"          |   "users"         || "DROP POLICY IF EXISTS users_policy ON public.users"
            "non_public_schema"     |   "users_policy"          |   "users"         || "DROP POLICY IF EXISTS users_policy ON non_public_schema.users"
            null                    |   "u_policy"              |   "users"         || "DROP POLICY IF EXISTS u_policy ON users"
            "public"                |   "u_policy"              |   "users"         || "DROP POLICY IF EXISTS u_policy ON public.users"
            "non_public_schema"     |   "u_policy"              |   "users"         || "DROP POLICY IF EXISTS u_policy ON non_public_schema.users"
            null                    |   "u_policy"              |   "users_groups"  || "DROP POLICY IF EXISTS u_policy ON users_groups"
            "public"                |   "u_policy"              |   "users_groups"  || "DROP POLICY IF EXISTS u_policy ON public.users_groups"
            "non_public_schema"     |   "u_policy"              |   "users_groups"  || "DROP POLICY IF EXISTS u_policy ON non_public_schema.users_groups"
    }

    def "should generate drop script for random values" ()
    {
        given:
            def r = new RandomString(12, new Random(), RandomString.lower)
            def policyName = r.nextString()
            def schema = r.nextString()
            def table = r.nextString()
            def expectedStatement = format("DROP POLICY IF EXISTS %1\$s ON %2\$s.%3\$s", policyName, schema, table)
            logger.log(java.util.logging.Level.INFO, "Random policy name: " + policyName)
            logger.log(java.util.logging.Level.INFO, "Random schema name: " + schema)
            logger.log(java.util.logging.Level.INFO, "Random table name: " + table)

        expect:
            tested.produce(builder().withPolicyName(policyName)
                    .withPolicySchema(schema)
                    .withPolicyTable(table)
                    .withGrantee("some_user")
                    .withPermissionCommandPolicy(ALL)
                    .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .build()).getDropScript() == expectedStatement
    }

    def "should generate the creation script for random values" ()
    {
        given:
            def r = new RandomString(12, new Random(), RandomString.lower)
            def policyName = r.nextString()
            def schema = r.nextString()
            def table = r.nextString()
            def grantee = r.nextString()
            def statementPattern = "CREATE POLICY %1\$s ON %2\$s.%3\$s\nFOR ALL\nTO \"%4\$s\"\nUSING (tenant_has_authorities_function(tenant_id, 'ALL', 'USING', '%3\$s', '%2\$s'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'ALL', 'WITH_CHECK', '%3\$s', '%2\$s'));"
            def expectedStatement = format(statementPattern, policyName, schema, table, grantee)
            logger.log(java.util.logging.Level.INFO, "Random policy name: " + policyName)
            logger.log(java.util.logging.Level.INFO, "Random schema name: " + schema)
            logger.log(java.util.logging.Level.INFO, "Random table name: " + table)
            logger.log(java.util.logging.Level.INFO, "Random grantee: " + grantee)

        expect:
            tested.produce(builder().withPolicyName(policyName)
                    .withPolicySchema(schema)
                    .withPolicyTable(table)
                    .withGrantee(grantee)
                    .withPermissionCommandPolicy(ALL)
                    .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                    .build()).getCreateScript() == expectedStatement
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

}
