package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;

public abstract class AbstractIdentifierValidator implements IIdentifierValidator{

    public abstract void init(SharedSchemaContextRequest sharedSchemaContextRequest) throws InvalidSharedSchemaContextRequestException;

}
