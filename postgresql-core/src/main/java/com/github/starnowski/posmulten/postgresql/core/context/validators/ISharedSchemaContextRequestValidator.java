package com.github.starnowski.posmulten.postgresql.core.context.validators;

import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

public interface ISharedSchemaContextRequestValidator {

    void validate(SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException;
}
