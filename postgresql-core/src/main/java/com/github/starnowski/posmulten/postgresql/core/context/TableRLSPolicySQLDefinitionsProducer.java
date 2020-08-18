package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducer;

import java.util.ArrayList;
import java.util.List;

public class TableRLSPolicySQLDefinitionsProducer {

    private RLSPolicyProducer rlsPolicyProducer = new RLSPolicyProducer();

    public List<SQLDefinition> produce(AbstractTableRLSPolicySQLDefinitionsProducerParameters parameters)
    {
        List<SQLDefinition> results = new ArrayList<>();
        return results;
    }

    void setRlsPolicyProducer(RLSPolicyProducer rlsPolicyProducer) {
        this.rlsPolicyProducer = rlsPolicyProducer;
    }
}
