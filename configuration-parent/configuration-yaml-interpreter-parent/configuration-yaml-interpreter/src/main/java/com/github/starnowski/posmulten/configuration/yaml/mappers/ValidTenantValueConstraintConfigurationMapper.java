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

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue;
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration;

public class ValidTenantValueConstraintConfigurationMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration, ValidTenantValueConstraintConfiguration> {
    @Override
    public ValidTenantValueConstraintConfiguration map(com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration input) {
        return input == null ? null : new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName(input.getIsTenantValidConstraintName() == null ? null : new StringWrapperWithNotBlankValue(input.getIsTenantValidConstraintName()))
                .setIsTenantValidFunctionName(input.getIsTenantValidFunctionName() == null ? null : new StringWrapperWithNotBlankValue(input.getIsTenantValidFunctionName()))
                .setTenantIdentifiersBlacklist(input.getTenantIdentifiersBlacklist());
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration unmap(ValidTenantValueConstraintConfiguration output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration()
                .setIsTenantValidConstraintName(output.getIsTenantValidConstraintName() == null ? null : output.getIsTenantValidConstraintName().getValue())
                .setIsTenantValidFunctionName(output.getIsTenantValidFunctionName() == null ? null : output.getIsTenantValidFunctionName().getValue())
                .setTenantIdentifiersBlacklist(output.getTenantIdentifiersBlacklist());
    }
}
