package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrimaryKeyDefinition {

    @JsonProperty(value = "pk_columns_name_to_type")
    private Map<@NotBlank String, @NotBlank String> primaryKeyColumnsNameToTypeMap;
    @NotBlank
    @JsonProperty(value = "name_for_function_that_checks_if_record_exists_in_table", required = true)
    private String nameForFunctionThatChecksIfRecordExistsInTable;
}
