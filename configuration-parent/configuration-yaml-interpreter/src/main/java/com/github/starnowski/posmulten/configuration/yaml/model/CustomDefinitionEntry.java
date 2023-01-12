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

@CustomPositionValidValue
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomDefinitionEntry {

    @NotNull
    @JsonProperty(value = "position", required = true)
    @EnumNamePattern(enumType = com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.class)
    private com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition position;
    @JsonProperty(value = "creation_script")
    private String creationScript;
    @JsonProperty(value = "drop_script")
    private String dropScript;
    @JsonProperty(value = "custom_position")
    private String customPosition;
    @JsonProperty(value = "validation_scripts")
    private List<String> validationScripts;
}
