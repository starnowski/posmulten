package com.github.starnowski.posmulten.configuration.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Optional;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ForeignKeyConfiguration {

    private String constraintName;
    private String tableName;
    private Optional<String> tableSchema;
    private Map<String, String> foreignKeyPrimaryKeyColumnsMappings;
}
