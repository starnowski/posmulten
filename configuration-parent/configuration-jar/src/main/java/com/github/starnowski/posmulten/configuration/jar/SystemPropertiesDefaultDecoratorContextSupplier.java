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

import com.github.starnowski.posmulten.configuration.SystemPropertyReader;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.starnowski.posmulten.configuration.jar.Constants.CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY;
import static com.github.starnowski.posmulten.configuration.jar.Constants.CONTEXT_DECORATOR_REPLACECHARACTERSMAP_SEPARATOR_PROPERTY;

public class SystemPropertiesDefaultDecoratorContextSupplier {

    private static final String REPLACECHARACTERSMAP_PAIR_PATTERN = "(.*)=(.*)";
    private static final Pattern replaceCharactersMapPattern = Pattern.compile(REPLACECHARACTERSMAP_PAIR_PATTERN);

    private final SystemPropertyReader systemPropertyReader;

    public SystemPropertiesDefaultDecoratorContextSupplier() {
        this(new SystemPropertyReader());
    }

    public SystemPropertiesDefaultDecoratorContextSupplier(SystemPropertyReader systemPropertyReader) {
        this.systemPropertyReader = systemPropertyReader;
    }

    public DefaultDecoratorContext get() {
        DefaultDecoratorContext.DefaultDecoratorContextBuilder decoratorContextBuilder = DefaultDecoratorContext.builder();
        String patternsValue = systemPropertyReader.read(CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY);
        if (patternsValue != null) {
            String separator = systemPropertyReader.read(CONTEXT_DECORATOR_REPLACECHARACTERSMAP_SEPARATOR_PROPERTY);
            String[] patterns = patternsValue.split(separator == null ? "," : separator);
            Map<String, String> replaceCharactersMap = new HashMap<>();
            for (String patternPari : patterns) {
                Matcher m = replaceCharactersMapPattern.matcher(patternPari);
                if (m.find()) {
                    replaceCharactersMap.put(m.group(1), m.group(2));
                }
            }
            decoratorContextBuilder.withReplaceCharactersMap(replaceCharactersMap);
        }
        return decoratorContextBuilder.build();
    }
}
