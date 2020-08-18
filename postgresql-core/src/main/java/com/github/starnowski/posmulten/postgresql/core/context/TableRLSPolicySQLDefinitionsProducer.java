package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducer;

import java.util.ArrayList;
import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder;
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.ALL;

public class TableRLSPolicySQLDefinitionsProducer {

    private RLSPolicyProducer rlsPolicyProducer = new RLSPolicyProducer();

    public List<SQLDefinition> produce(AbstractTableRLSPolicySQLDefinitionsProducerParameters parameters)
    {
        List<SQLDefinition> results = new ArrayList<>();
        SQLDefinition sqlDefinition = rlsPolicyProducer.produce(builder().withPolicyName(parameters.getPolicyName())
                .withPolicySchema(parameters.getTableKey().getSchema())
                .withPolicyTable(parameters.getTableKey().getTable())
                .withGrantee(parameters.getGrantee())
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(parameters.getTenantHasAuthoritiesFunctionInvocationFactory())
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(parameters.getTenantHasAuthoritiesFunctionInvocationFactory())
                .withTenantIdColumn(parameters.getTenantIdColumn() == null ? parameters.getDefaultTenantIdColumn() : parameters.getTenantIdColumn())
                .build());
        results.add(sqlDefinition);
        return results;
    }

    void setRlsPolicyProducer(RLSPolicyProducer rlsPolicyProducer) {
        this.rlsPolicyProducer = rlsPolicyProducer;
    }
}
