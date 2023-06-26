package com.github.starnowski.posmulten.configuration.jar;

import com.github.starnowski.posmulten.configuration.DDLScriptsGenerator;
import com.github.starnowski.posmulten.configuration.NoDefaultSharedSchemaContextBuilderFactorySupplierException;
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import static com.github.starnowski.posmulten.configuration.jar.Constants.*;

@Log
public class DDLScriptsGeneratorRunner {

    public static void main(String[] args) throws IOException {
        if (Boolean.TRUE.equals(Boolean.valueOf(System.getProperty(PRINT_PROJECT_VERSION_PROPERTY)))) {
            java.io.InputStream is = DDLScriptsGeneratorRunner.class.getClassLoader().getResourceAsStream("configuration-jar.properties");
            java.util.Properties p = new Properties();
            p.load(is);
            String version = p.getProperty(PROJECT_VERSION_PROPERTY);
            System.out.print(version);
        } else if (Boolean.TRUE.equals(Boolean.valueOf(System.getProperty("posmulten.configuration.config.yaml.syntax.guide.print")))) {
            java.io.InputStream input = DDLScriptsGeneratorRunner.class.getClassLoader().getResourceAsStream("configuration-yaml-interpreter-readme.txt");
            byte[] buffer = new byte[8192];
            try {
                for (int length = 0; (length = input.read(buffer)) != -1; ) {
                    System.out.write(buffer, 0, length);
                }
            } finally {
                input.close();
            }
        } else {
            String configFilePath = System.getProperty(CONFIG_FILE_PATH_PROPERTY);
            String createScriptPath = System.getProperty(CREATE_SCRIPT_PATH_PROPERTY);
            String dropScriptPath = System.getProperty(DROP_SCRIPT_PATH_PROPERTY);
            String validationStatementsPath = System.getProperty(VALIDATION_STATEMENTS_PATH_PROPERTY);
            DDLScriptsGenerator ddlScriptsGenerator = new DDLScriptsGenerator();
            try {
                ddlScriptsGenerator.generate(configFilePath, createScriptPath, dropScriptPath, validationStatementsPath);
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
