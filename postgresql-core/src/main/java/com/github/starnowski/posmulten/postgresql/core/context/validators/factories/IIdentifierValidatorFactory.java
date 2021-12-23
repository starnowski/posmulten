package com.github.starnowski.posmulten.postgresql.core.context.validators.factories;

import com.github.starnowski.posmulten.postgresql.core.context.IIdentifierValidator;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;

public interface IIdentifierValidatorFactory <T extends IIdentifierValidator> {

    T build(SharedSchemaContextRequest sharedSchemaContextRequest);
}
