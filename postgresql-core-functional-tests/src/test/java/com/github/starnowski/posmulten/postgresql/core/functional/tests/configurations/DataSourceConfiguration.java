package com.github.starnowski.posmulten.postgresql.core.functional.tests.configurations;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.owner")
    public DataSourceProperties ownerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.owner.configuration")
    public DataSource ownerDataSource() {
        return ownerDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "ownerJdbcTemplate")
    @Primary
    public JdbcTemplate ownerJdbcTemplate() {
        return new JdbcTemplate(ownerDataSource());
    }
}