package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum
import spock.lang.Shared
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.*
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.USING
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.WITH_CHECK

class TenantHasAuthoritiesFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new TenantHasAuthoritiesFunctionProducer()

    @Shared
    def firstEqualsCurrentTenantIdentifierFunctionInvocationFactory = { tenant ->
        "is_tenant_starts_with_abcd(" + (FunctionArgumentValueEnum.STRING.equals(tenant.getType()) ? ("'" + tenant.getValue() + "'") : tenant.getValue()) + ")"
    }
    @Shared
    def secondEqualsCurrentTenantIdentifierFunctionInvocationFactory = { tenant ->
        "matches_current_tenant(" + (FunctionArgumentValueEnum.STRING.equals(tenant.getType()) ? ("'" + tenant.getValue() + "'") : tenant.getValue()) + ")"
    }

    @Unroll
    def "should generate statement that creates function for parameters object '#parametersObject' and return expected statement '#expectedStatement'" () {
        expect:
            tested.produce(parametersObject).getCreateScript() == expectedStatement

        where:
            parametersObject <<     [
                                        //1
                                        builder().withFunctionName("tenant_has_authorities").withSchema(null)
                                            .withEqualsCurrentTenantIdentifierFunctionInvocationFactory(firstEqualsCurrentTenantIdentifierFunctionInvocationFactory)
                                            .withTenantIdArgumentType("VARCHAR(312)")
                                            .withPermissionCommandPolicyArgumentType("text")
                                            .withRlsExpressionArgumentType("VARCHAR(73)")
                                            .withTableArgumentType("text")
                                            .withSchemaArgumentType("VARCHAR(117)")
                                            .build(),
                                        //2
                                        builder().withFunctionName("this_tenant_has_authorities").withSchema("secondary_schema")
                                             .withEqualsCurrentTenantIdentifierFunctionInvocationFactory(secondEqualsCurrentTenantIdentifierFunctionInvocationFactory)
                                             .withTenantIdArgumentType("VARCHAR(55)")
                                             .withPermissionCommandPolicyArgumentType("VARCHAR(534)")
                                             .withRlsExpressionArgumentType("text")
                                             .withTableArgumentType("VARCHAR(231)")
                                             .withSchemaArgumentType("text")
                                             .build()
                                    ]
            expectedStatement <<    [
                                        //1
                                        "CREATE OR REPLACE FUNCTION tenant_has_authorities(VARCHAR(312), text, VARCHAR(73), text, VARCHAR(117)) RETURNS BOOLEAN AS \$\$" +
                                        "\nSELECT is_tenant_starts_with_abcd(\$1)" +
                                        "\n\$\$ LANGUAGE sql" +
                                        "\nSTABLE" +
                                        "\nPARALLEL SAFE;",
                                        //2
                                        "CREATE OR REPLACE FUNCTION secondary_schema.this_tenant_has_authorities(VARCHAR(55), VARCHAR(534), text, VARCHAR(231), text) RETURNS BOOLEAN AS \$\$" +
                                                "\nSELECT matches_current_tenant(\$1)" +
                                                "\n\$\$ LANGUAGE sql" +
                                                "\nSTABLE" +
                                                "\nPARALLEL SAFE;"
                                    ]
    }

    @Unroll
    def "should generate statement that invokes function for schema #schema, with function name #functionName, tenat id value '#tenantValue', permission command #permissionCommand, RLS expression type #rlsExpression, table #table and schema #argumentSchema"()
    {
        given:
            tested.produce(new TenantHasAuthoritiesFunctionProducerParameters(functionName, schema, secondEqualsCurrentTenantIdentifierFunctionInvocationFactory)).returnTenantHasAuthoritiesFunctionInvocation(tenantValue, permissionCommand, rlsExpression, table, argumentSchema) == expectedStatement

        where:
            schema      |   functionName                |   tenantValue             |   permissionCommand   |   rlsExpression   |   table                           |   argumentSchema              ||  expectedStatement
            null        |   "tenant_has_authorities"    |   forString("DDDS-AA")    |   ALL                 |   USING           |   forString("users")              |   forString("public")         ||  "tenant_has_authorities('DDDS-AA', 'ALL', 'USING', 'users', 'public')"
            "public"    |   "has_authorities"           |   forString("XXX22")      | DELETE                | WITH_CHECK        |   forString("notifications")      |   forString("public")         ||  "public.has_authorities('XXX22', 'DELETE', 'WITH_CHECK', 'notifications', 'public')"
            "secondary" |   "has_auth"                  |   forString("XXX22")      | INSERT                | USING             |   forString("roles")              |   forString("public")         ||  "secondary.has_auth('XXX22', 'INSERT', 'USING', 'roles', 'public')"
            "third"     |   "t_h_a"                     |   forString("XXX22")      | UPDATE                | USING             |   forString("users")              |   forString("public")         ||  "third.t_h_a('XXX22', 'UPDATE', 'USING', 'users', 'public')"
            "third"     |   "t_h_a"                     |   forReference("\$1")     | SELECT                | WITH_CHECK        |   forString("users")              |   forString("public")         ||  "third.t_h_a(\$1, 'SELECT', 'WITH_CHECK', 'users', 'public')"
            "public"    |   "tenant_has_authorities"    |   forReference("tenant")  | ALL                   | WITH_CHECK        |   forString("users")              |   forString("public")         ||  "public.tenant_has_authorities(tenant, 'ALL', 'WITH_CHECK', 'users', 'public')"
            "public"    |   "tenant_has_authorities"    |   forReference("tenant")  | ALL                   | WITH_CHECK        |   forReference("table_variable")  |   forString("public")         ||  "public.tenant_has_authorities(tenant, 'ALL', 'WITH_CHECK', table_variable, 'public')"
    }

    private TenantHasAuthoritiesFunctionProducerParameters.TenantHasAuthoritiesFunctionProducerParametersBuilder builder()
    {
        new TenantHasAuthoritiesFunctionProducerParameters.TenantHasAuthoritiesFunctionProducerParametersBuilder()
    }

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(TenantHasAuthoritiesFunctionProducerParameters, constructorArgs: ["tenant_has_authorities", "public", firstEqualsCurrentTenantIdentifierFunctionInvocationFactory, "text", "VARCHAR(13)", "VARCHAR(512)", "VARCHAR(128)", "VARCHAR(32)"])
    }
}
