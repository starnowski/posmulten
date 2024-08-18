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
package com.github.starnowski.posmulten.configuration.yaml.core.mappers;

import com.github.starnowski.posmulten.configuration.yaml.core.model.*;

import static java.util.stream.Collectors.toList;

public abstract class AbstractTableEntryMapper<S extends AbstractStringWrapperWithNotBlankValue, PK extends AbstractPrimaryKeyDefinition<PK>, RLSP extends AbstractRLSPolicy<S, PK, RLSP>, FKC extends AbstractForeignKeyConfiguration<FKC>, T extends AbstractTableEntry<RLSP, FKC, T>> extends AbstractConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.TableEntry, T> {

    protected abstract AbstractRLSPolicyMapper<S, PK, RLSP> getRLRlsPolicyMapper();
    protected abstract AbstractForeignKeyConfigurationMapper<FKC> getForeignKeyConfigurationMapper();

    @Override
    public T map(com.github.starnowski.posmulten.configuration.core.model.TableEntry input) {
        return input == null ? null : createNewInstanceOfOutput().setName(input.getName())
                .setSchema(input.getSchema())
                .setRlsPolicy(getRLRlsPolicyMapper().map(input.getRlsPolicy()))
                .setForeignKeys(input.getForeignKeys() == null ? null : input.getForeignKeys().stream().map(key -> getForeignKeyConfigurationMapper().map(key)).collect(toList()));
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.TableEntry unmap(T output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.TableEntry().setName(output.getName()).setRlsPolicy(getRLRlsPolicyMapper().unmap(output.getRlsPolicy()))
                .setSchema(output.getSchema())
                .setForeignKeys(output.getForeignKeys() == null ? null : output.getForeignKeys().stream().map(key -> getForeignKeyConfigurationMapper().unmap(key)).collect(toList()));
    }
}
