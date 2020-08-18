package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory;

public interface AbstractTableRLSPolicySQLDefinitionsProducerParameters {

    String getGrantee();

    TableKey getTableKey();

    String getPolicyName();

    TenantHasAuthoritiesFunctionInvocationFactory getTenantHasAuthoritiesFunctionInvocationFactory();

    String getTenantIdColumn();
}
