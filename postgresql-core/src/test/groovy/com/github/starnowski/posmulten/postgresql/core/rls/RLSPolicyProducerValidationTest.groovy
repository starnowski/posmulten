package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducerParameters
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.ALL

class RLSPolicyProducerValidationTest extends Specification {

    private static final String IS_TENANT_ID_CORRECT_TEST_FUNCTION = "is_tenant_starts_with_abcd"
    private static final String TENANT_HAS_AUTHORITIES_TEST_FUNCTION = "tenant_has_authorities_function"

    TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory1
    def tested = new RLSPolicyProducer()

    def setup()
    {
        EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory = prepareEqualsCurrentTenantIdentifierFunctionInvocationFactoryForTest()
        TenantHasAuthoritiesFunctionProducer producer = new TenantHasAuthoritiesFunctionProducer()
        def tenantHasAuthoritiesFunction = producer.produce(new TenantHasAuthoritiesFunctionProducerParameters(TENANT_HAS_AUTHORITIES_TEST_FUNCTION, null, equalsCurrentTenantIdentifierFunctionInvocationFactory))

        tenantHasAuthoritiesFunctionInvocationFactory1 = tenantHasAuthoritiesFunction
    }

    def "should generate the creation script for builder with correct values" ()
    {
        expect:
            tested.produce(prepareBuilderWithCorrectValues().build()).getCreateScript() == "CREATE POLICY table_policy ON some_schema.table\nFOR ALL\nTO \"post-user\"\nUSING (tenant_has_authorities_function(tenant_id, 'ALL', 'USING', 'table', 'some_schema'))\nWITH CHECK (tenant_has_authorities_function(tenant_id, 'ALL', 'WITH_CHECK', 'table', 'some_schema'));"
    }

    def "should throw exception of type 'IllegalArgumentException' when parameters object is null"()
    {
        when:
            tested.produce(null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Parameters object cannot be null"
    }

    def "should throw exception of type 'IllegalArgumentException' when policy name is null"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withPolicyName(null).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Policy name cannot be null"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when policy name is blank (#policyName)"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withPolicyName(policyName).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Policy name cannot be blank"

        where:
            policyName << ["", " ", "      "]
    }

    def "should throw exception of type 'IllegalArgumentException' when policy table is null"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withPolicyTable(null).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Policy table cannot be null"
    }

    def prepareBuilderWithCorrectValues()
    {
        builder().withPolicyName("table_policy")
                .withPolicySchema("some_schema")
                .withPolicyTable("table")
                .withGrantee("post-user")
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory1)
    }

    private Closure<String> prepareEqualsCurrentTenantIdentifierFunctionInvocationFactoryForTest() {
        { tenant ->
            IS_TENANT_ID_CORRECT_TEST_FUNCTION + "(" + mapToString(tenant) + ")"
        }
    }
}
