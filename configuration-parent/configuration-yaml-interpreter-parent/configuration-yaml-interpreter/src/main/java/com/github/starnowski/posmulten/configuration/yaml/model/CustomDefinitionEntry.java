/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractCustomDefinitionEntry;
import com.github.starnowski.posmulten.configuration.yaml.validation.CustomPositionValidValue;
import com.github.starnowski.posmulten.configuration.yaml.validation.EnumNamePattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@CustomPositionValidValue
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomDefinitionEntry implements AbstractCustomDefinitionEntry<CustomDefinitionEntry> {

    private final static Set<String> correctCustomPositionValues = Stream.of(com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.values()).map(Enum::name).collect(toSet());;

    @NotNull
    @JsonProperty(value = "position", required = true)
    @EnumNamePattern(enumType = com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.class)
    private String position;
    @NotBlank
    @JsonProperty(value = "creation_script", required = true)
    private String creationScript;
    @JsonProperty(value = "drop_script")
    private String dropScript;
    @JsonProperty(value = "custom_position")
    private String customPosition;
    @NotEmpty
    @JsonProperty(value = "validation_scripts", required = true)
    private List<String> validationScripts;

    public com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition getPosition() {
        return isValidCustomDefinitionPosition(position) ? com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.valueOf(position) : null;
    }

    public CustomDefinitionEntry setPosition(com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition position) {
        this.position = position == null ? null : position.name();
        return this;
    }

    public CustomDefinitionEntry setPosition(String position) {
        this.position = position;
        return this;
    }

    private boolean isValidCustomDefinitionPosition(String value){
        return correctCustomPositionValues.contains(value);
    }

}
