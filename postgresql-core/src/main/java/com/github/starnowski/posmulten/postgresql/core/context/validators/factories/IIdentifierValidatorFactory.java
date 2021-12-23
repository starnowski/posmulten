package com.github.starnowski.posmulten.postgresql.core.context.validators.factories;

import com.github.starnowski.posmulten.postgresql.core.context.IIdentifierValidator;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;

public interface IIdentifierValidatorFactory <T extends IIdentifierValidator> {

    T build(SharedSchemaContextRequest sharedSchemaContextRequest) throws InvalidSharedSchemaContextRequestException;
}
