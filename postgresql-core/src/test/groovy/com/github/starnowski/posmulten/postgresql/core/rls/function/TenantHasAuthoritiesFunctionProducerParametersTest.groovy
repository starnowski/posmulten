package com.github.starnowski.posmulten.postgresql.core.rls.function

import spock.lang.Specification

class TenantHasAuthoritiesFunctionProducerParametersTest extends Specification {

    def "should return correct value fot the toString method"() {
        given:
            def functionName = "tenant_is_privilege"
            def schema = "pub_sche"
            def tenantIdArgumentType = "VARCHAR(13)"
            def permissionCommandPolicyArgumentType = "VARCHAR(57)"
            def rlsExpressionArgumentType = "VARCHAR(233)"
            def tableArgumentType = "VARCHAR(512)"
            def schemaArgumentType = "VARCHAR(10)"

        when:
            def parameters = new TenantHasAuthoritiesFunctionProducerParameters.TenantHasAuthoritiesFunctionProducerParametersBuilder()
                    .withFunctionName(functionName)
                    .withSchema(schema)
                    .withTenantIdArgumentType(tenantIdArgumentType)
                    .withPermissionCommandPolicyArgumentType(permissionCommandPolicyArgumentType)
                    .withRlsExpressionArgumentType(rlsExpressionArgumentType)
                    .withTableArgumentType(tableArgumentType)
                    .withSchemaArgumentType(schemaArgumentType)
                    .build()
            def result = parameters.toString()

        then:
            result.contains("functionName='tenant_is_privilege'")
            result.contains("schema='pub_sche'")
            result.contains("tenantIdArgumentType='VARCHAR(13)'")
            result.contains("permissionCommandPolicyArgumentType='VARCHAR(57)'")
            result.contains("rlsExpressionArgumentType='VARCHAR(233)'")
            result.contains("tableArgumentType='VARCHAR(512)'")
            result.contains("schemaArgumentType='VARCHAR(10)'")
            result.matches("TenantHasAuthoritiesFunctionProducerParameters\\{.*}")
    }
}
