/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableEntry implements AbstractTableEntry<TableEntry, RLSPolicy, ForeignKeyConfiguration> {
    /**
     * Table name
     */
    @NotBlank
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonProperty(value = "schema")
    private Optional<String> schema;
    @Valid
    @JsonProperty(value = "rls_policy")
    private RLSPolicy rlsPolicy;
    @JsonProperty(value = "foreign_keys")
    private List<@Valid ForeignKeyConfiguration> foreignKeys;
    @Override
    public TableEntry setForeignKeys(List<ForeignKeyConfiguration> foreignKeys) {
        this.foreignKeys = foreignKeys;
        return this;
    }
}
