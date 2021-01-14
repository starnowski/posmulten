package com.github.starnowski.posmulten.configuration.jar;

import com.github.starnowski.posmulten.configuration.DDLScriptsGenerator;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import java.io.IOException;

public class DDLScriptsGeneratorRunner {

    public static void main(String[] args) throws SharedSchemaContextBuilderException, IOException {
        String configFilePath = System.getProperty("posmulten.configuration.config.file.path");
        String createScriptPath = System.getProperty("posmulten.configuration.create.script.path");
        String dropScriptPath = System.getProperty("posmulten.configuration.drop.script.path");
        DDLScriptsGenerator ddlScriptsGenerator = new DDLScriptsGenerator();
        ddlScriptsGenerator.generate(configFilePath, createScriptPath, dropScriptPath);
    }
}
