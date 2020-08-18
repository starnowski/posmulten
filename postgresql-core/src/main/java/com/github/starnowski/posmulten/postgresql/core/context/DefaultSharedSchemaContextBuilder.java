package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.enrichers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class DefaultSharedSchemaContextBuilder {

    private String defaultSchema;
    private List<AbstractSharedSchemaContextEnricher> enrichers = asList(new GetCurrentTenantIdFunctionDefinitionEnricher(), new SetCurrentTenantIdFunctionDefinitionEnricher(), new TenantHasAuthoritiesFunctionDefinitionEnricher(), new TenantColumnSQLDefinitionsEnricher(), new TableRLSSettingsSQLDefinitionsEnricher(), new TableRLSPolicyEnricher());
    private SharedSchemaContextRequest sharedSchemaContextRequest = new SharedSchemaContextRequest();

    public DefaultSharedSchemaContextBuilder(String defaultSchema) {
        this.defaultSchema = defaultSchema;
        this.sharedSchemaContextRequest.setDefaultSchema(defaultSchema);
    }

    public AbstractSharedSchemaContext build()
    {
        AbstractSharedSchemaContext context = new SharedSchemaContext();
        List<AbstractSharedSchemaContextEnricher> enrichers  = getEnrichers();
        //TODO Copy request
        sharedSchemaContextRequest.setDefaultSchema(defaultSchema);
        for (AbstractSharedSchemaContextEnricher enricher : enrichers)
        {
            //TODO Consider of copy request (in loop also)
            context = enricher.enrich(context, sharedSchemaContextRequest);
        }
        return context;
    }

    public List<AbstractSharedSchemaContextEnricher> getEnrichers() {
        return enrichers == null ? new ArrayList<>() : new ArrayList<>(enrichers);
    }

    public DefaultSharedSchemaContextBuilder setEnrichers(List<AbstractSharedSchemaContextEnricher> enrichers) {
        this.enrichers = enrichers;
        return this;
    }

    public SharedSchemaContextRequest getSharedSchemaContextRequest() {
        return sharedSchemaContextRequest;
    }

    public DefaultSharedSchemaContextBuilder setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        sharedSchemaContextRequest.setCurrentTenantIdPropertyType(currentTenantIdPropertyType);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setCurrentTenantIdProperty(String currentTenantIdProperty) {
        sharedSchemaContextRequest.setCurrentTenantIdProperty(currentTenantIdProperty);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setGetCurrentTenantIdFunctionName(getCurrentTenantIdFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setSetCurrentTenantIdFunctionName(setCurrentTenantIdFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setEqualsCurrentTenantIdentifierFunctionName(String equalsCurrentTenantIdentifierFunctionName) {
        sharedSchemaContextRequest.setEqualsCurrentTenantIdentifierFunctionName(equalsCurrentTenantIdentifierFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setTenantHasAuthoritiesFunctionName(String tenantHasAuthoritiesFunctionName) {
        sharedSchemaContextRequest.setTenantHasAuthoritiesFunctionName(tenantHasAuthoritiesFunctionName);
        return this;
    }

    public DefaultSharedSchemaContextBuilder createTenantColumnForTable(String table)
    {
        TableKey tableKey = new TableKey(table, this.defaultSchema);
        sharedSchemaContextRequest.getCreateTenantColumnTableLists().add(tableKey);
        return this;
    }

    public DefaultSharedSchemaContextBuilder createRLSPolicyForColumn(String table, Map<String, String> primaryKeyColumnsList, String tenantColumnName, String rlsPolicyName)
    {
        TableKey tableKey = new TableKey(table, this.defaultSchema);
        sharedSchemaContextRequest.getTableColumnsList().put(tableKey, new DefaultTableColumns(tenantColumnName, primaryKeyColumnsList));
        sharedSchemaContextRequest.getTableRLSPolicies().put(tableKey, new DefaultTableRLSPolicyProperties(rlsPolicyName));
        return this;
    }

    public DefaultSharedSchemaContextBuilder setForceRowLevelSecurityForTableOwner(boolean forceRowLevelSecurityForTableOwner) {
        sharedSchemaContextRequest.setForceRowLevelSecurityForTableOwner(forceRowLevelSecurityForTableOwner);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setDefaultTenantIdColumn(String defaultTenantIdColumn) {
        sharedSchemaContextRequest.setDefaultTenantIdColumn(defaultTenantIdColumn);
        return this;
    }

    public DefaultSharedSchemaContextBuilder setGrantee(String grantee) {
        sharedSchemaContextRequest.setGrantee(grantee);
        return this;
    }
}
