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

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.write;
import static java.util.stream.Collectors.toList;

public class DDLWriter {

    public void saveCreteScripts(String filePath, ISharedSchemaContext sharedSchemaContext) throws IOException {
        List<String> lines = sharedSchemaContext.getSqlDefinitions().stream().map(SQLDefinition::getCreateScript).collect(toList());
        write(Paths.get(new File(filePath).toURI()), lines);
    }

    public void saveDropScripts(String filePath, ISharedSchemaContext sharedSchemaContext) throws IOException {
        //Save DDL statements in reverse order
        LinkedList<SQLDefinition> stack = new LinkedList<>();
        sharedSchemaContext.getSqlDefinitions().forEach(stack::push);
        List<String> lines = stack.stream().map(SQLDefinition::getDropScript).collect(toList());
        write(Paths.get(new File(filePath).toURI()), lines);
    }

    public void saveCheckingStatements(String filePath, ISharedSchemaContext sharedSchemaContext) throws IOException {
        List<String> lines = sharedSchemaContext.getSqlDefinitions().stream().map(SQLDefinition::getCheckingStatements).filter(Objects::nonNull).flatMap(Collection::stream).collect(toList());
        write(Paths.get(new File(filePath).toURI()), lines);
    }
}
