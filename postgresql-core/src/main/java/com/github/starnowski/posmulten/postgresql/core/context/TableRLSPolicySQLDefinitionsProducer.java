/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
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
