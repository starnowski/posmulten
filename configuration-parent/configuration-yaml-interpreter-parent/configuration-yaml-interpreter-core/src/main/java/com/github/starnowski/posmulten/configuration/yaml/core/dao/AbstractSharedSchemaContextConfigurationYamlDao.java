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
package com.github.starnowski.posmulten.configuration.yaml.core.dao;

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.core.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSharedSchemaContextConfiguration;

import java.io.IOException;

public interface AbstractSharedSchemaContextConfigurationYamlDao<T extends AbstractSharedSchemaContextConfiguration> {

     T read(String filePath) throws IOException, YamlInvalidSchema;

    T readFromContent(String content) throws IOException, YamlInvalidSchema;

    void save(T configuration, String filePath) throws IOException, YamlInvalidSchema;
}
