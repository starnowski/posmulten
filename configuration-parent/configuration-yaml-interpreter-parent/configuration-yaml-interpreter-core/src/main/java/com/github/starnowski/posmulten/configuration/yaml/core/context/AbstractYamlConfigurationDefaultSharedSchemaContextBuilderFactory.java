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
package com.github.starnowski.posmulten.configuration.yaml.core.context;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.core.dao.AbstractSharedSchemaContextConfigurationYamlDao;
import com.github.starnowski.posmulten.configuration.yaml.core.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractSharedSchemaContextConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.core.model.*;

import java.io.IOException;

public abstract class AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactory<PK extends AbstractPrimaryKeyDefinition<PK>,  FKC extends AbstractForeignKeyConfiguration<FKC>, TE extends AbstractTableEntry<RLSP, FKC, TE>, SWWNBV extends AbstractStringWrapperWithNotBlankValue, RLSP extends AbstractRLSPolicy<SWWNBV, PK, RLSP>, CDE extends AbstractCustomDefinitionEntry<CDE>, VTVCC extends AbstractValidTenantValueConstraintConfiguration<VTVCC, SWWNBV>, SDV extends AbstractSqlDefinitionsValidation<SDV>, T extends AbstractSharedSchemaContextConfiguration<SWWNBV, CDE, VTVCC, TE, SDV, T>, SSCCYD extends AbstractSharedSchemaContextConfigurationYamlDao<T>, SSCCM extends AbstractSharedSchemaContextConfigurationMapper<PK,  FKC, TE, SWWNBV, RLSP, CDE, VTVCC, SDV , T>> extends AbstractDefaultSharedSchemaContextBuilderFactory {

    protected abstract SSCCYD getSharedSchemaContextConfigurationYamlDao();
    protected abstract SSCCM getSharedSchemaContextConfigurationMapper();

    @Override
    protected SharedSchemaContextConfiguration prepareConfigurationBasedOnContent(String content) throws InvalidConfigurationException {
        T yamlConfiguration = null;
        try {
            yamlConfiguration = getSharedSchemaContextConfigurationYamlDao().readFromContent(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getSharedSchemaContextConfigurationMapper().unmap(yamlConfiguration);
    }

    @Override
    protected SharedSchemaContextConfiguration prepareConfigurationBasedOnFile(String filePath) throws YamlInvalidSchema {
        T yamlConfiguration = null;
        try {
            yamlConfiguration = getSharedSchemaContextConfigurationYamlDao().read(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getSharedSchemaContextConfigurationMapper().unmap(yamlConfiguration);
    }
}
