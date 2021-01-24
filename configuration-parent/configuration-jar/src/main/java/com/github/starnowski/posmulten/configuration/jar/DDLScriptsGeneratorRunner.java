package com.github.starnowski.posmulten.configuration.jar;

import com.github.starnowski.posmulten.configuration.DDLScriptsGenerator;
import com.github.starnowski.posmulten.configuration.NoDefaultSharedSchemaContextBuilderFactorySupplierException;
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

@Log
public class DDLScriptsGeneratorRunner {

    public static void main(String[] args) throws IOException {
        if (Boolean.TRUE.equals(Boolean.valueOf(System.getProperty("posmulten.configuration.config.version.print")))) {
            java.io.InputStream is = DDLScriptsGeneratorRunner.class.getClassLoader().getResourceAsStream("configuration-jar.properties");
            java.util.Properties p = new Properties();
            p.load(is);
            String version = p.getProperty("configuration.jar.project.version");
            System.out.print(version);
        } else {
            String configFilePath = System.getProperty("posmulten.configuration.config.file.path");
            String createScriptPath = System.getProperty("posmulten.configuration.create.script.path");
            String dropScriptPath = System.getProperty("posmulten.configuration.drop.script.path");
            DDLScriptsGenerator ddlScriptsGenerator = new DDLScriptsGenerator();
            try {
                ddlScriptsGenerator.generate(configFilePath, createScriptPath, dropScriptPath);
            } catch (InvalidConfigurationException e) {
                log.log(Level.SEVERE, "Posmulten invalid configuration");
                e.getErrorMessages().forEach(message ->
                        log.log(Level.SEVERE, "Configuration error: {0}", message)
                );
                System.exit(1);
            } catch (SharedSchemaContextBuilderException | NoDefaultSharedSchemaContextBuilderFactorySupplierException e) {
                log.log(Level.SEVERE, "Posmulten invalid configuration");
                log.log(Level.SEVERE, "Configuration error: {0}", e.getMessage());
                System.exit(1);
            }
        }
    }
}
