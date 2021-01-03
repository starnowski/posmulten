package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForeignKeyConfiguration {

    @JsonProperty(value = "constraint_name", required = true)
    @NotBlank
    private String constraintName;
    @NotBlank
    @JsonProperty(value = "table_name", required = true)
    private String tableName;
    @JsonProperty(value = "foreign_key_primary_key_columns_mappings", required = true)
    @NotNull
    @Size(min = 1)
    private Map<String, String> foreignKeyPrimaryKeyColumnsMappings;
}
