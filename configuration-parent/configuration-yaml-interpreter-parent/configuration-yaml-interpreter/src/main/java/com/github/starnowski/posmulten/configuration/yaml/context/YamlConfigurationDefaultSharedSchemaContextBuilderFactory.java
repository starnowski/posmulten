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
package com.github.starnowski.posmulten.configuration.yaml.context;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.dao.SharedSchemaContextConfigurationYamlDao;
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.configuration.yaml.mappers.SharedSchemaContextConfigurationMapper;

import java.io.IOException;

public class YamlConfigurationDefaultSharedSchemaContextBuilderFactory extends AbstractDefaultSharedSchemaContextBuilderFactory {

    private final SharedSchemaContextConfigurationYamlDao dao = new SharedSchemaContextConfigurationYamlDao();
    private final SharedSchemaContextConfigurationMapper mapper = new SharedSchemaContextConfigurationMapper();

    @Override
    protected SharedSchemaContextConfiguration prepareConfigurationBasedOnContent(String content) throws InvalidConfigurationException {
        com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration yamlConfiguration = null;
        try {
            yamlConfiguration = dao.readFromContent(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapper.unmap(yamlConfiguration);
    }

    @Override
    protected SharedSchemaContextConfiguration prepareConfigurationBasedOnFile(String filePath) throws YamlInvalidSchema {
        com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration yamlConfiguration = null;
        try {
            yamlConfiguration = dao.read(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapper.unmap(yamlConfiguration);
    }
}
