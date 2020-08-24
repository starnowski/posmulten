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
import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedSchemaContext implements AbstractSharedSchemaContext {

    private IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;
    private ISetCurrentTenantIdFunctionInvocationFactory iSetCurrentTenantIdFunctionInvocationFactory;
    private TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory;
    private List<SQLDefinition> sqlDefinitions = new ArrayList<>();
    private Map<TableKey, IsRecordBelongsToCurrentTenantFunctionInvocationFactory> tableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap = new HashMap();

    @Override
    public List<SQLDefinition> getSqlDefinitions() {
        return new ArrayList<>(sqlDefinitions);
    }

    @Override
    public void addSQLDefinition(SQLDefinition sqlDefinition) {
        sqlDefinitions.add(sqlDefinition);
    }

    @Override
    public TenantHasAuthoritiesFunctionInvocationFactory getTenantHasAuthoritiesFunctionInvocationFactory() {
        return tenantHasAuthoritiesFunctionInvocationFactory;
    }

    @Override
    public void setTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory) {
        this.tenantHasAuthoritiesFunctionInvocationFactory = tenantHasAuthoritiesFunctionInvocationFactory;
    }

    @Override
    public IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory() {
        return iGetCurrentTenantIdFunctionInvocationFactory;
    }

    @Override
    public void setIGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory factory) {
        this.iGetCurrentTenantIdFunctionInvocationFactory = factory;
    }

    @Override
    public void setISetCurrentTenantIdFunctionInvocationFactory(ISetCurrentTenantIdFunctionInvocationFactory factory) {
        this.iSetCurrentTenantIdFunctionInvocationFactory = factory;
    }

    @Override
    public ISetCurrentTenantIdFunctionInvocationFactory getISetCurrentTenantIdFunctionInvocationFactory() {
        return iSetCurrentTenantIdFunctionInvocationFactory;
    }

    @Override
    public Map<TableKey, IsRecordBelongsToCurrentTenantFunctionInvocationFactory> getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap() {
        return tableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap;
    }
}
