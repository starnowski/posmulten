package com.github.starnowski.posmulten.postgresql.core.context.validators;

import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableThatRequiredTenatColumnCreationException;

import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

public class CreateTenantColumnTableMappingSharedSchemaContextRequestValidator implements ISharedSchemaContextRequestValidator{
    @Override
    public void validate(SharedSchemaContextRequest request) throws MissingRLSPolicyDeclarationForTableThatRequiredTenatColumnCreationException {
        if (!request.getCreateTenantColumnTableLists().isEmpty())
        {
            Set<TableKey> rlsTables = request.getTableColumnsList().keySet();
            Optional<TableKey> tableWithRLSPolicyDeclaration = request.getCreateTenantColumnTableLists().stream().filter(tableKey -> !rlsTables.contains(tableKey)).findFirst();
            if (tableWithRLSPolicyDeclaration.isPresent())
            {
                TableKey table = tableWithRLSPolicyDeclaration.get();
                throw new MissingRLSPolicyDeclarationForTableThatRequiredTenatColumnCreationException(table, format("Missing RLS policy declaration for table %1$s in schema %2$s for which creation of tenant column was requested", table.getTable(), table.getSchema()));
            }
        }
    }
}
