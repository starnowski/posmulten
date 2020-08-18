package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducer
import spock.lang.Specification
import spock.lang.Unroll

class TableRLSPolicySQLDefinitionsProducerTest extends Specification {

    def tested = new TableRLSPolicySQLDefinitionsProducer()

    @Unroll
    def "should create all required SQL definition that creates RLS policy for table #tableKey, grantee #grantee witn policy name #policyName"()
    {
        given:
            def rlsPolicyProducer = Mock(RLSPolicyProducer)
            def rlsPolicyProducerSQLDefinition = Mock(SQLDefinition)
            def tenantHasAuthoritiesFunctionInvocationFactory = Mock(TenantHasAuthoritiesFunctionInvocationFactory)
            tested.setRlsPolicyProducer(rlsPolicyProducer)
            def parameters = new TableRLSPolicySQLDefinitionsProducerParameters.TableRLSPolicySQLDefinitionsProducerParametersBuilder()
                    .withGrantee(grantee)
                    .withTableKey(tableKey)
                    .withPolicyName(policyName)
                    .withTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                    .build()

        when:
            def results = tested.produce(parameters)

        then:
            1 * rlsPolicyProducer.produce(tableKey.getTable(), tableKey.getSchema()) >> rlsPolicyProducerSQLDefinition
            results == [rlsPolicyProducerSQLDefinition]

        where:
            tableKey                |   grantee     |   policyName
            tk("users", null)       |   "core-user" |   "some_rls_policy"
            tk("users", "public")   |   "owner"     |   "some_rls_policy"
            tk("posts", "public")   |   "owner"     |   "posts_policy"
            tk("posts", "some_sh")  |   "owner"     |   "posts_policy"
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
