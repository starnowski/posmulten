package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.DDLWriter;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import java.io.IOException;

public class DDLScriptsGenerator {

    public DDLScriptsGenerator() {
        this(new DefaultSharedSchemaContextBuilderFactoryResolver(), new DDLWriter());
    }

    public DDLScriptsGenerator(DefaultSharedSchemaContextBuilderFactoryResolver defaultSharedSchemaContextBuilderFactoryResolver, DDLWriter ddlWriter) {
        this.defaultSharedSchemaContextBuilderFactoryResolver = defaultSharedSchemaContextBuilderFactoryResolver;
        this.ddlWriter = ddlWriter;
    }

    private final DefaultSharedSchemaContextBuilderFactoryResolver defaultSharedSchemaContextBuilderFactoryResolver;
    private final DDLWriter ddlWriter;

    public void generate(String configurationFilePath, String createScripsFilePath, String dropScripsFilePath) throws SharedSchemaContextBuilderException, IOException {
        IDefaultSharedSchemaContextBuilderFactory factory = defaultSharedSchemaContextBuilderFactoryResolver.resolve(configurationFilePath);
        DefaultSharedSchemaContextBuilder builder = factory.build(configurationFilePath);
        ISharedSchemaContext context = builder.build();
        if (createScripsFilePath != null) {
            ddlWriter.saveCreteScripts(createScripsFilePath, context);
        }
        if (dropScripsFilePath != null) {
            ddlWriter.saveDropScripts(dropScripsFilePath, context);
        }
    }
}
