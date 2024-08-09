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
package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy;
import com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue;

public class RLSPolicyMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.RLSPolicy, RLSPolicy> {

    private final PrimaryKeyDefinitionMapper primaryKeyDefinitionMapper = new PrimaryKeyDefinitionMapper();

    @Override
    public RLSPolicy map(com.github.starnowski.posmulten.configuration.core.model.RLSPolicy input) {
        return input == null ? null : new RLSPolicy()
                .setName(input.getName())
                .setCreateTenantColumnForTable(input.getCreateTenantColumnForTable())
                .setPrimaryKeyDefinition(primaryKeyDefinitionMapper.map(input.getPrimaryKeyDefinition()))
                .setSkipAddingOfTenantColumnDefaultValue(input.getSkipAddingOfTenantColumnDefaultValue())
                .setTenantColumn(input.getTenantColumn() == null ? null : new StringWrapperWithNotBlankValue(input.getTenantColumn()))
                .setValidTenantValueConstraintName(input.getValidTenantValueConstraintName() == null ? null : new StringWrapperWithNotBlankValue(input.getValidTenantValueConstraintName()));
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.RLSPolicy unmap(RLSPolicy output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.RLSPolicy()
                .setName(output.getName())
                .setCreateTenantColumnForTable(output.getCreateTenantColumnForTable())
                .setPrimaryKeyDefinition(primaryKeyDefinitionMapper.unmap(output.getPrimaryKeyDefinition()))
                .setSkipAddingOfTenantColumnDefaultValue(output.getSkipAddingOfTenantColumnDefaultValue())
                .setTenantColumn(output.getTenantColumn() == null ? null : output.getTenantColumn().getValue())
                .setValidTenantValueConstraintName(output.getValidTenantValueConstraintName() == null ? null : output.getValidTenantValueConstraintName().getValue());
    }
}
