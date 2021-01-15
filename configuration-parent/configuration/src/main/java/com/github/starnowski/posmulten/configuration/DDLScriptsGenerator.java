package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.DDLWriter;
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.logging.Level;

@Log
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
        log.log(Level.INFO, "Generate DDL statements based on file: {0}", new Object[]{configurationFilePath});
        IDefaultSharedSchemaContextBuilderFactory factory = defaultSharedSchemaContextBuilderFactoryResolver.resolve(configurationFilePath);
        DefaultSharedSchemaContextBuilder builder = factory.build(configurationFilePath);
        ISharedSchemaContext context = builder.build();
        if (createScripsFilePath != null) {
            log.log(Level.INFO, "Saving DDL statements that creates the shared schema strategy to {0}", createScripsFilePath);
            ddlWriter.saveCreteScripts(createScripsFilePath, context);
        }
        if (dropScripsFilePath != null) {
            log.log(Level.INFO, "Saving DDL statements that drop the shared schema strategy to {0}", dropScripsFilePath);
            ddlWriter.saveDropScripts(dropScripsFilePath, context);
        }
    }
}
