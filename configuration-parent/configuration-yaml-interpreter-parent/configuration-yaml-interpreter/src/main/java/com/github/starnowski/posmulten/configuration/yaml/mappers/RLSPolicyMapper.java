/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition;
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractRLSPolicyMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy;
import com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue;

public class RLSPolicyMapper extends AbstractRLSPolicyMapper<StringWrapperWithNotBlankValue, com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition, RLSPolicy> {

    private final PrimaryKeyDefinitionMapper primaryKeyDefinitionMapper = new PrimaryKeyDefinitionMapper();

    @Override
    protected StringWrapperWithNotBlankValue createNewStringWrapperWithNotBlankValue(String value) {
        return new StringWrapperWithNotBlankValue(value);
    }

    @Override
    protected IConfigurationMapper<PrimaryKeyDefinition, com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition> getPrimaryKeyDefinitionMapper() {
        return primaryKeyDefinitionMapper;
    }

    @Override
    protected RLSPolicy createNewInstanceOfOutput() {
        return new RLSPolicy();
    }
}
