package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.List;

/**
 * Defines position for custom SQL definition passed to query builder.
 * @see DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(CustomSQLDefinitionPairPositionProvider, String)
 * @see DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(CustomSQLDefinitionPairPositionProvider, String, String)
 * @see DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(CustomSQLDefinitionPairPositionProvider, String, String, List)
 * @see DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(CustomSQLDefinitionPairPositionProvider, SQLDefinition)
 */
public interface CustomSQLDefinitionPairPositionProvider {

    /**
     * @return position for custom SQL definition
     */
    String getPosition();
}
