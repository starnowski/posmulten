package com.github.starnowski.posmulten.postgresql.core.context.validators.factories;

import com.github.starnowski.posmulten.postgresql.core.context.IdentifierLengthValidator;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;

public class IdentifierLengthValidatorFactory implements IIdentifierValidatorFactory<IdentifierLengthValidator>{
    @Override
    public IdentifierLengthValidator build(SharedSchemaContextRequest sharedSchemaContextRequest) {
        return null;
    }
}
