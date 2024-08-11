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

import com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractForeignKeyConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractRLSPolicyMapper;
import com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractTableEntryMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.*;

public class TableEntryMapper extends AbstractTableEntryMapper<StringWrapperWithNotBlankValue, PrimaryKeyDefinition, RLSPolicy, ForeignKeyConfiguration, TableEntry> {

    private final RLSPolicyMapper rlsPolicyMapper = new RLSPolicyMapper();
    private final ForeignKeyConfigurationMapper foreignKeyConfigurationMapper = new ForeignKeyConfigurationMapper();

    @Override
    protected AbstractRLSPolicyMapper<StringWrapperWithNotBlankValue, PrimaryKeyDefinition, RLSPolicy> getRLRlsPolicyMapper() {
        return rlsPolicyMapper;
    }

    @Override
    protected AbstractForeignKeyConfigurationMapper<ForeignKeyConfiguration> getForeignKeyConfigurationMapper() {
        return foreignKeyConfigurationMapper;
    }

    @Override
    protected TableEntry createNewInstanceOfOutput() {
        return new TableEntry();
    }
}
