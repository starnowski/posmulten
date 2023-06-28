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
package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.DDLWriter;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.logging.Level;

@Log
public class DDLScriptsGenerator {

    private final DefaultSharedSchemaContextBuilderFactoryResolver defaultSharedSchemaContextBuilderFactoryResolver;
    private final DDLWriter ddlWriter;
    private final SharedSchemaContextDecoratorFactory sharedSchemaContextDecoratorFactory;

    public DDLScriptsGenerator() {
        this(new DefaultSharedSchemaContextBuilderFactoryResolver(), new DDLWriter());
    }

    public DDLScriptsGenerator(DefaultSharedSchemaContextBuilderFactoryResolver defaultSharedSchemaContextBuilderFactoryResolver, DDLWriter ddlWriter) {
        this(defaultSharedSchemaContextBuilderFactoryResolver, ddlWriter, new SharedSchemaContextDecoratorFactory());
    }

    public DDLScriptsGenerator(DefaultSharedSchemaContextBuilderFactoryResolver defaultSharedSchemaContextBuilderFactoryResolver, DDLWriter ddlWriter, SharedSchemaContextDecoratorFactory sharedSchemaContextDecoratorFactory) {
        this.defaultSharedSchemaContextBuilderFactoryResolver = defaultSharedSchemaContextBuilderFactoryResolver;
        this.ddlWriter = ddlWriter;
        this.sharedSchemaContextDecoratorFactory = sharedSchemaContextDecoratorFactory;
    }

    public void generate(String configurationFilePath, String createScripsFilePath, String dropScripsFilePath, String checkingStatementsFilePath) throws SharedSchemaContextBuilderException, IOException, InvalidConfigurationException, NoDefaultSharedSchemaContextBuilderFactorySupplierException {
        this.generate(configurationFilePath, createScripsFilePath, dropScripsFilePath, checkingStatementsFilePath, null);
    }

    public void generate(String configurationFilePath, String createScripsFilePath, String dropScripsFilePath, String checkingStatementsFilePath, DefaultDecoratorContext decoratorContext) throws SharedSchemaContextBuilderException, IOException, InvalidConfigurationException, NoDefaultSharedSchemaContextBuilderFactorySupplierException {
        log.log(Level.INFO, "Generate DDL statements based on file: {0}", new Object[]{configurationFilePath});
        IDefaultSharedSchemaContextBuilderFactory factory = defaultSharedSchemaContextBuilderFactoryResolver.resolve(configurationFilePath);
        DefaultSharedSchemaContextBuilder builder = factory.build(configurationFilePath);
        ISharedSchemaContext context = builder.build();
        if (decoratorContext != null) {
            context = sharedSchemaContextDecoratorFactory.build(context, decoratorContext);
        }
        if (createScripsFilePath != null) {
            log.log(Level.INFO, "Saving DDL statements that creates the shared schema strategy to {0}", createScripsFilePath);
            ddlWriter.saveCreteScripts(createScripsFilePath, context);
        }
        if (dropScripsFilePath != null) {
            log.log(Level.INFO, "Saving DDL statements that drop the shared schema strategy to {0}", dropScripsFilePath);
            ddlWriter.saveDropScripts(dropScripsFilePath, context);
        }
        if (checkingStatementsFilePath != null) {
            log.log(Level.INFO, "Saving checking statements that checks if the changes were applied to {0}", checkingStatementsFilePath);
            ddlWriter.saveCheckingStatements(checkingStatementsFilePath, context);
        }
    }
}
