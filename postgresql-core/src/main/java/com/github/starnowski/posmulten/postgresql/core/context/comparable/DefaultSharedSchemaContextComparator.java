package com.github.starnowski.posmulten.postgresql.core.context.comparable;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.stream.Collectors;

public class DefaultSharedSchemaContextComparator implements SharedSchemaContextComparator {
    @Override
    public SharedSchemaContextComparableResults diff(ISharedSchemaContext left, ISharedSchemaContext right) {
        return new SharedSchemaContextComparableResults(
                new ComparableResult(left.getSqlDefinitions().stream().map(def -> def.getCreateScript()).filter(cre -> right.getSqlDefinitions().stream().map(def1 -> def1.getCreateScript()).noneMatch(cre1 -> cre.equals(cre1))).collect(Collectors.toList())
                        , right.getSqlDefinitions().stream().map(def -> def.getCreateScript()).filter(cre -> left.getSqlDefinitions().stream().map(def1 -> def1.getCreateScript()).noneMatch(cre1 -> cre.equals(cre1))).collect(Collectors.toList())), null, null);
    }
}
