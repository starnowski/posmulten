package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomDefinitionEntry {

    @JsonProperty(value = "position", required = true)
    @NotBlank
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
