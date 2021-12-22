package com.github.starnowski.posmulten.postgresql.core.context.validators;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import java.util.List;

public class FunctionDefinitionValidator implements ISQLDefinitionsValidator{
    @Override
    public void validate(List<SQLDefinition> sqlDefinitions) throws SharedSchemaContextBuilderException {
        //TODO
    }
}
