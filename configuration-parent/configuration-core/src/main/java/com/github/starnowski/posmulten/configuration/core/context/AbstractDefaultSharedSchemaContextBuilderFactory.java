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
package com.github.starnowski.posmulten.configuration.core.context;

import com.github.starnowski.posmulten.configuration.core.DefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public abstract class AbstractDefaultSharedSchemaContextBuilderFactory implements IDefaultSharedSchemaContextBuilderFactory{

    private final DefaultSharedSchemaContextBuilderFactory defaultSharedSchemaContextBuilderFactory = new DefaultSharedSchemaContextBuilderFactory();

    @Override
    public DefaultSharedSchemaContextBuilder build(String filePath) throws InvalidConfigurationException {
        SharedSchemaContextConfiguration contextConfiguration = prepareConfigurationBasedOnFile(filePath);
        return defaultSharedSchemaContextBuilderFactory.build(contextConfiguration);
    }

    protected abstract SharedSchemaContextConfiguration prepareConfigurationBasedOnFile(String filePath) throws InvalidConfigurationException;
}
