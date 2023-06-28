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
package com.github.starnowski.posmulten.configuration.jar;

public class Constants {

    public static final String CONFIG_FILE_PATH_PROPERTY = "posmulten.configuration.config.file.path";
    public static final String CREATE_SCRIPT_PATH_PROPERTY = "posmulten.configuration.create.script.path";
    public static final String DROP_SCRIPT_PATH_PROPERTY = "posmulten.configuration.drop.script.path";
    public static final String VALIDATION_STATEMENTS_PATH_PROPERTY = "posmulten.configuration.validation.statements.path";
    public static final String PRINT_PROJECT_VERSION_PROPERTY = "posmulten.configuration.config.version.print";
    public static final String PROJECT_VERSION_PROPERTY = "configuration.jar.project.version";
    public static final String CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY = "posmulten.configuration.config.context.decorator.replaceCharactersMap";
    public static final String CONTEXT_DECORATOR_REPLACECHARACTERSMAP_SEPARATOR_PROPERTY = "posmulten.configuration.config.context.decorator.replaceCharactersMap.separator";
}
