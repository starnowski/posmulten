package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation;

public class SqlDefinitionsValidationMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation, SqlDefinitionsValidation> {
    @Override
    public SqlDefinitionsValidation map(com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation input) {
        return input == null ? null : new SqlDefinitionsValidation().setDisabled(input.getDisabled()).setIdentifierMaxLength(input.getIdentifierMaxLength()).setIdentifierMinLength(input.getIdentifierMinLength());
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation unmap(SqlDefinitionsValidation output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation().setDisabled(output.getDisabled()).setIdentifierMaxLength(output.getIdentifierMaxLength()).setIdentifierMinLength(output.getIdentifierMinLength());
    }
}
