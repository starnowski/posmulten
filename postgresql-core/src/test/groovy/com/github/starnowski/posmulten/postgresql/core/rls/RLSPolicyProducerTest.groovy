package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducerParameters
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.*

class RLSPolicyProducerTest extends Specification {

    private static final String IS_TENANT_ID_CORRECT_TEST_FUNCTION = "is_tenant_starts_with_abcd"
    private static final String TENANT_HAS_AUTHORITIES_TEST_FUNCTION = "tenant_has_authorities_function"
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
            "public"                |   "users_policy"          |   "users"         |   "postgresql-core-user"  |   INSERT              || "CREATE POLICY users_policy ON users\nFOR INSERT\nTO \"postgresql-core-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'INSERT', 'USING', 'users', 'public'));"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "postgresql-core-user"  |   SELECT              || "sssss"
            null                    |   "users_policy"          |   "users"         |   "postgresql-core-owner" |   UPDATE              || "sssss"
            "public"                |   "users_policy"          |   "users"         |   "postgresql-core-owner" |   DELETE              || "sssss"
            "non_public_schema"     |   "users_policy"          |   "users"         |   "postgresql-core-owner" |   ALL                 || "sssss"
            null                    |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"  |   INSERT              || "sssss"
            "public"                |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"  |   SELECT              || "sssss"
            "non_public_schema"     |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-user"  |   UPDATE              || "sssss"
            null                    |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner" |   DELETE              || "sssss"
            "public"                |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner" |   ALL                 || "sssss"
            "non_public_schema"     |   "users_groups_policy"   |   "users_groups"  |   "postgresql-core-owner" |   INSERT              || "sssss"
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
