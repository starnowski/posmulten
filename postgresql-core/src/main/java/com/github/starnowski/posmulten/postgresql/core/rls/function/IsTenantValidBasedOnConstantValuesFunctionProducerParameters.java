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
package com.github.starnowski.posmulten.postgresql.core.rls.function;

import java.util.Set;

/**
 * @since 0.2
 */
public class IsTenantValidBasedOnConstantValuesFunctionProducerParameters implements IIsTenantValidBasedOnConstantValuesFunctionProducerParameters{

    private final String functionName;
    private final String schema;
    private final Set<String> blacklistTenantIds;
    private final String argumentType;

    public IsTenantValidBasedOnConstantValuesFunctionProducerParameters(String functionName, String schema, Set<String> blacklistTenantIds, String argumentType) {
        this.functionName = functionName;
        this.schema = schema;
        this.blacklistTenantIds = blacklistTenantIds;
        this.argumentType = argumentType;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public Set<String> getBlacklistTenantIds() {
        return blacklistTenantIds;
    }

    @Override
    public String getArgumentType() {
        return argumentType;
    }
}
