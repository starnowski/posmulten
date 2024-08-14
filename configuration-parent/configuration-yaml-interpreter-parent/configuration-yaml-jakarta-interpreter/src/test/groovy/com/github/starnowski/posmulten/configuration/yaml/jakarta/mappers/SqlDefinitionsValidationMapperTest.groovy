package com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractSqlDefinitionsValidationMapperTest
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.SqlDefinitionsValidation

class SqlDefinitionsValidationMapperTest extends AbstractSqlDefinitionsValidationMapperTest<SqlDefinitionsValidation, SqlDefinitionsValidationMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected Class<SqlDefinitionsValidation> getYamlConfigurationObjectClass() {
        SqlDefinitionsValidation.class
    }

    @Override
    protected SqlDefinitionsValidationMapper getTestedObject() {
        new SqlDefinitionsValidationMapper()
    }

    @Override
    protected SqlDefinitionsValidation createOutputInstance() {
        new SqlDefinitionsValidation()
    }
}
