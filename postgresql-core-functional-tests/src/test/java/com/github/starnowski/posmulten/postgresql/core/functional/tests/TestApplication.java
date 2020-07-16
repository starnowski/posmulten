package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {

    public static final String CLEAR_DATABASE_SCRIPT_PATH = "com/github/starnowski/posmulten/postgresql/core/functional/tests/clean-database.sql";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
