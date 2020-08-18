package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;

public class TableRLSPolicyEnricher implements AbstractSharedSchemaContextEnricher {

    private TableRLSPolicySQLDefinitionsProducer tableRLSPolicySQLDefinitionsProducer = new TableRLSPolicySQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        request.getTableColumnsList().entrySet().forEach(entry ->
        {
            TableRLSPolicySQLDefinitionsProducerParameters.TableRLSPolicySQLDefinitionsProducerParametersBuilder builder = new TableRLSPolicySQLDefinitionsProducerParameters.TableRLSPolicySQLDefinitionsProducerParametersBuilder();
            TableRLSPolicySQLDefinitionsProducerParameters parameters = builder
                    .withDefaultTenantIdColumn(request.getDefaultTenantIdColumn())
                    .withGrantee(request.getGrantee())
                    .withPolicyName(request.getTableRLSPolicies().get(entry.getKey()).getPolicyName())
                    .withTableKey(entry.getKey())
                    .withTenantHasAuthoritiesFunctionInvocationFactory(context.getTenantHasAuthoritiesFunctionInvocationFactory())
                    .withTenantIdColumn(entry.getValue().getTenantColumnName())
                    .build();
            tableRLSPolicySQLDefinitionsProducer.produce(parameters).forEach(context::addSQLDefinition);
        });
        return context;
    }

    void setTableRLSPolicySQLDefinitionsProducer(TableRLSPolicySQLDefinitionsProducer tableRLSPolicySQLDefinitionsProducer) {
        this.tableRLSPolicySQLDefinitionsProducer = tableRLSPolicySQLDefinitionsProducer;
    }
}
