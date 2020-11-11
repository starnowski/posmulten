package com.github.starnowski.posmulten.postgresql.core.context.validators;

import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

public class TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidator implements ISharedSchemaContextRequestValidator{
    @Override
    public void validate(SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        //TODO
    }

}
