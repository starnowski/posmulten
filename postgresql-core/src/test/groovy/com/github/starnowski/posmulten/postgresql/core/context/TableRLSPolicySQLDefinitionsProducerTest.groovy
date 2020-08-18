package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum
import com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducer
import com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

class TableRLSPolicySQLDefinitionsProducerTest extends Specification {

    def tested = new TableRLSPolicySQLDefinitionsProducer()

    @Unroll
    def "should create all required SQL definition that creates RLS policy for table #tableKey, grantee #grantee with policy name #policyName and expected tenant column #expectedTenantIdColumn"()
    {
        given:
            RLSPolicyProducerParameters capturedParameters = null
            def rlsPolicyProducer = Mock(RLSPolicyProducer)
            def rlsPolicyProducerSQLDefinition = Mock(SQLDefinition)
            def tenantHasAuthoritiesFunctionInvocationFactory = Mock(TenantHasAuthoritiesFunctionInvocationFactory)
            tested.setRlsPolicyProducer(rlsPolicyProducer)
            def parameters = new TableRLSPolicySQLDefinitionsProducerParameters.TableRLSPolicySQLDefinitionsProducerParametersBuilder()
                    .withGrantee(grantee)
                    .withTableKey(tableKey)
                    .withPolicyName(policyName)
                    .withTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                    .withTenantIdColumn(tenantIdColumn)
                    .withDefaultTenantIdColumn(defaultTenantIdColumn)
                    .build()

        when:
            def results = tested.produce(parameters)

        then:
            1 * rlsPolicyProducer.produce(_) >>  {
                    passedParameters ->
                    capturedParameters = passedParameters[0]
                    rlsPolicyProducerSQLDefinition
            }
            results == [rlsPolicyProducerSQLDefinition]

        and: "pass correct parameters"
            capturedParameters.getGrantee() == grantee
            capturedParameters.getPolicyTable() == tableKey.getTable()
            capturedParameters.getPolicySchema() == tableKey.getSchema()
            capturedParameters.getPolicyName() == policyName
            capturedParameters.getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory() == tenantHasAuthoritiesFunctionInvocationFactory
            capturedParameters.getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory() == tenantHasAuthoritiesFunctionInvocationFactory
            capturedParameters.getPermissionCommandPolicy() == PermissionCommandPolicyEnum.ALL
            capturedParameters.getTenantIdColumn() == expectedTenantIdColumn

        where:
            tableKey                |   grantee     |   policyName          |   tenantIdColumn  |   defaultTenantIdColumn   ||  expectedTenantIdColumn
            tk("users", null)       |   "core-user" |   "some_rls_policy"   |   null            |   "tenant"                ||  "tenant"
            tk("users", "public")   |   "owner"     |   "some_rls_policy"   |   "tenant_id"     |   "tenant"                ||  "tenant_id"
            tk("posts", "public")   |   "owner"     |   "posts_policy"      |   "tenant"        |   "some_name"             ||  "tenant"
            tk("posts", "some_sh")  |   "owner"     |   "posts_policy"      |   "col_ten_"      |   "tenant"                ||  "col_ten_"
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
