package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.DDLWriter;

public class DDLScriptsGenerator {

    public DDLScriptsGenerator()
    {
        this(new DefaultSharedSchemaContextBuilderFactoryResolver(), new DDLWriter());
    }

    public DDLScriptsGenerator(DefaultSharedSchemaContextBuilderFactoryResolver defaultSharedSchemaContextBuilderFactoryResolver, DDLWriter ddlWriter) {
        this.defaultSharedSchemaContextBuilderFactoryResolver = defaultSharedSchemaContextBuilderFactoryResolver;
        this.ddlWriter = ddlWriter;
    }

    private final DefaultSharedSchemaContextBuilderFactoryResolver defaultSharedSchemaContextBuilderFactoryResolver;
    private final DDLWriter ddlWriter;

    public void generate(String configurationFilePath, String createScripsFilePath, String dropScripsFilePath)
    {
        //TODO
    }
}
