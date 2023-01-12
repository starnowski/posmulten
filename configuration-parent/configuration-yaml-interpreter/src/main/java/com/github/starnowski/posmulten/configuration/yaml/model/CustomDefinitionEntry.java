package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.starnowski.posmulten.configuration.yaml.validation.CustomPositionValidValue;
import com.github.starnowski.posmulten.configuration.yaml.validation.EnumNamePattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
public class CustomDefinitionEntry {

    private final static Set<String> correctCustomPositionValues = Stream.of(com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.values()).map(Enum::name).collect(toSet());;

    @NotNull
    @JsonProperty(value = "position", required = true)
    @EnumNamePattern(enumType = com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.class)
    private String position;
    @JsonProperty(value = "creation_script", required = true)
    private String creationScript;
    @JsonProperty(value = "drop_script")
    private String dropScript;
    @JsonProperty(value = "custom_position")
    private String customPosition;
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
