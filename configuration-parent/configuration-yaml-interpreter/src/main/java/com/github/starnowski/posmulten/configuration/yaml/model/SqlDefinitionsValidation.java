package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SqlDefinitionsValidation {

    @Min(1)
    @JsonProperty(value = "identifier_max_length")
    private Integer identifierMaxLength;
    @Min(1)
    @JsonProperty(value = "identifier_min_length")
    private Integer identifierMinLength;
    @JsonProperty(value = "disabled")
    private Boolean disabled;
}
