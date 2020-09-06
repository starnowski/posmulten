package com.github.starnowski.posmulten.postgresql.core.context.validators;

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.IncorrectForeignKeysMappingException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableException;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ForeignKeysMappingSharedSchemaContextRequestValidator implements ISharedSchemaContextRequestValidator{
    @Override
    public void validate(SharedSchemaContextRequest request) throws IncorrectForeignKeysMappingException, MissingRLSPolicyDeclarationForTableException {
        List<Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>> constraintsRequests = request.getSameTenantConstraintForForeignKeyProperties().entrySet().stream().map(entry -> new Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>(entry.getKey(), entry.getValue())).collect(toList());
        Map<TableKey, ITableColumns> primaryKeyDefinitions = request.getTableColumnsList();
        for (Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties> constrainRequest : constraintsRequests)
        {
            Set<String> primaryKeysInForeignKeysMapping = constrainRequest.getValue().getForeignKeyPrimaryKeyColumnsMappings().values().stream().collect(Collectors.toSet());
            ITableColumns rlsPolicy = primaryKeyDefinitions.get(constrainRequest.getKey().getForeignKeyTable());
            if (rlsPolicy == null)
            {
                TableKey tableKey = constrainRequest.getKey().getForeignKeyTable();
                throw new MissingRLSPolicyDeclarationForTableException(tableKey, format("Missing RLS policy declaration for table %1$s in schema %2$s", tableKey.getTable(), tableKey.getSchema()));
            }
            Set<String> primaryKeysForRLSPolicy = rlsPolicy.getIdentityColumnNameAndTypeMap().keySet();
            if (!primaryKeysInForeignKeysMapping.equals(primaryKeysForRLSPolicy))
            {
                throw new IncorrectForeignKeysMappingException(prepareExceptionMessage(constrainRequest.getKey().getMainTable(), constrainRequest.getKey().getForeignKeyTable(), primaryKeysInForeignKeysMapping, primaryKeysForRLSPolicy),
                        constrainRequest.getKey().getMainTable(),
                        constrainRequest.getKey().getForeignKeyTable(),
                        primaryKeysInForeignKeysMapping,
                        primaryKeysForRLSPolicy);
            }
        }
    }

    private String prepareExceptionMessage(TableKey foreignTableKey, TableKey primaryTableKey, Set<String> foreignKeys, Set<String> primaryKeys)
    {
        return format("There is mismatch between foreign keys column mapping (%1$s) in %2$s table and primary keys column declaration (%3$s) for %4$s table",
                foreignKeys.stream().sorted().collect(joining(", ")),
                returnTableName(foreignTableKey),
                primaryKeys.stream().sorted().collect(joining(", ")),
                returnTableName(primaryTableKey));
    }

    private String returnTableName(TableKey tableKey)
    {
        return (tableKey.getSchema() == null ? "" : tableKey.getSchema() + ".") + tableKey.getTable();
    }
}
