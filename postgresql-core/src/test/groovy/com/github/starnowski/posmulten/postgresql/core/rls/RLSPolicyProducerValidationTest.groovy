package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducerParameters
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.ALL
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.INSERT

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

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when policy table is blank (#policyTable)"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withPolicyTable(policyTable).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Policy table cannot be blank"

        where:
            policyTable << ["", " ", "      "]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when policy schema is blank (#policySchema)"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withPolicySchema(policySchema).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Policy schema cannot be blank"

        where:
            policySchema << ["", " ", "      "]
    }

    def "should throw exception of type 'IllegalArgumentException' when grantee is null"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withGrantee(null).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Grantee cannot be null"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when grantee is blank (#grantee)"()
    {
        given:
        def parameters = prepareBuilderWithCorrectValues().withGrantee(grantee).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Grantee cannot be blank"

        where:
            grantee << ["", " ", "      "]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when tenant id column is blank (#tenantIdColumn)"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withTenantIdColumn(tenantIdColumn).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Tenant id column cannot be blank"

        where:
            tenantIdColumn << ["", " ", "      "]
    }

    def "should throw exception of type 'IllegalArgumentException' when permission command policy is null"()
    {
        given:
            def parameters = prepareBuilderWithCorrectValues().withPermissionCommandPolicy(null).build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Permission command policy cannot be null"
    }

    def "should throw exception of type 'IllegalArgumentException' when the USING and CHECK WITH expressions are null"()
    {
        given:
        def parameters = prepareBuilderWithCorrectValues()
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(null)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(null)
                .build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The components for the USING and the CHECK WITH expressions cannot be null"
    }

    def "should throw exception of type 'IllegalArgumentException' when the permission command policy is INSERT and the CHECK WITH expressions is null"()
    {
        given:
        def parameters = prepareBuilderWithCorrectValues()
                .withPermissionCommandPolicy(INSERT)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(null)
                .build()

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "For the INSERT permission command the CHECK WITH expressions cannot be null"
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
