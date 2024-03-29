package com.github.starnowski.posmulten.configuration.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {

    public static final String CLEAR_DATABASE_SCRIPT_PATH = "/com/github/starnowski/posmulten/configuration/yaml/clean-database.sql";
    public static final String INTEGRATION_CONFIGURATION_TEST_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/integration-tests-configuration.yaml";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
