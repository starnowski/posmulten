package com.github.starnowski.posmulten.configuration.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TableEntry {

    /**
     * Table name
     */
    private String name;
    private RLSPolicy rlsPolicy;
    private List<ForeignKeyConfiguration> foreignKeys;
}
