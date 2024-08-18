package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSqlDefinitionsValidation

abstract class AbstractSqlDefinitionsValidationMapperTest <T extends AbstractSqlDefinitionsValidation, M extends IConfigurationMapper< SqlDefinitionsValidation, T>, CMTC extends AbstractConfigurationMapperTestContext> extends AbstractConfigurationMapperTest<T, SqlDefinitionsValidation, M, CMTC> {
    @Override
    protected Class<SqlDefinitionsValidation> getConfigurationObjectClass() {
        SqlDefinitionsValidation.class
    }

    protected abstract T createOutputInstance()

    @Override
    protected List<T> prepareExpectedMappedObjectsList() {
        [
                createOutputInstance(),
                createOutputInstance().setDisabled(true),
                createOutputInstance().setDisabled(false),
                createOutputInstance().setIdentifierMaxLength(1),
                createOutputInstance().setIdentifierMinLength(5),
                createOutputInstance().setIdentifierMinLength(3).setIdentifierMaxLength(11)
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
