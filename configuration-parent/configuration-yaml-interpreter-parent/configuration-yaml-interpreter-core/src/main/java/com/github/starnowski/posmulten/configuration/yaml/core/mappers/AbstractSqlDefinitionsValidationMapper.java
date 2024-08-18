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

import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSqlDefinitionsValidation;

public abstract class AbstractSqlDefinitionsValidationMapper<T extends AbstractSqlDefinitionsValidation<T>> extends AbstractConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation, T> {
    @Override
    public T map(com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation input) {
        return input == null ? null : createNewInstanceOfOutput().setDisabled(input.getDisabled()).setIdentifierMaxLength(input.getIdentifierMaxLength()).setIdentifierMinLength(input.getIdentifierMinLength());
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation unmap(T output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation().setDisabled(output.getDisabled()).setIdentifierMaxLength(output.getIdentifierMaxLength()).setIdentifierMinLength(output.getIdentifierMinLength());
    }
}
