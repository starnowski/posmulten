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
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;

import java.util.List;
import java.util.Map;

public interface ISharedSchemaContext {

    /**
     * Getting a list of objects of type {@link SQLDefinition} that represents DDL changes that should be applied.
     * The list's order is crucial because objects are added to how DDL statements should be applied.
     * This means that in case of dropping changes, the DDL instructions should be applied in reverse order.
     * @return list of objects that represents DDL statements that should be applied
     * @see SQLDefinition
     */
    List<SQLDefinition> getSqlDefinitions();

    /**
     * Adding an object of type SQLDefinition to the list that is returned by the {@link #getSqlDefinitions()} method.
     * The object is added to the end of that list.
     * This is crucial because objects are added in order of how DDL statements should be applied.
     * @param sqlDefinition object of type {@link SQLDefinition} that represents DDL statement that should be applied
     */
    void addSQLDefinition(SQLDefinition sqlDefinition);

    TenantHasAuthoritiesFunctionInvocationFactory getTenantHasAuthoritiesFunctionInvocationFactory();

    void setTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory factory);

    IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory();

    void setIGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory factory);

    void setISetCurrentTenantIdFunctionInvocationFactory(ISetCurrentTenantIdFunctionInvocationFactory factory);

    void setISetCurrentTenantIdFunctionPreparedStatementInvocationFactory(ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory factory);

    ISetCurrentTenantIdFunctionInvocationFactory getISetCurrentTenantIdFunctionInvocationFactory();

    ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory();

    Map<TableKey, IsRecordBelongsToCurrentTenantFunctionInvocationFactory> getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap();

    IIsTenantValidFunctionInvocationFactory getIIsTenantValidFunctionInvocationFactory();

    void setIIsTenantValidFunctionInvocationFactory(IIsTenantValidFunctionInvocationFactory factory);
}
