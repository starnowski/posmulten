package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation

class SqlDefinitionsValidationMapperTest extends AbstractConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation, com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation, SqlDefinitionsValidationMapper> {
    @Override
    protected Class<SqlDefinitionsValidation> getConfigurationObjectClass() {
        SqlDefinitionsValidation.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation.class
    }

    @Override
    protected SqlDefinitionsValidationMapper getTestedObject() {
        new SqlDefinitionsValidationMapper()
    }

    @Override
    protected List<com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation> prepareExpectedMappedObjectsList() {
        [
                new com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation(),
                new com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation().setDisabled(true),
                new com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation().setDisabled(false),
                new com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation().setIdentifierMaxLength(1),
                new com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation().setIdentifierMinLength(5),
                new com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation().setIdentifierMinLength(3).setIdentifierMaxLength(11)
        ]
    }

    @Override
    protected List<SqlDefinitionsValidation> prepareExpectedUnmappeddObjectsList() {
        [
                new SqlDefinitionsValidation(),
                new SqlDefinitionsValidation().setDisabled(true),
                new SqlDefinitionsValidation().setDisabled(false),
                new SqlDefinitionsValidation().setIdentifierMaxLength(1),
                new SqlDefinitionsValidation().setIdentifierMinLength(5),
                new SqlDefinitionsValidation().setIdentifierMinLength(3).setIdentifierMaxLength(11)
        ]
    }
}
