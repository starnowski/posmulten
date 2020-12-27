package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableEntry {

    /**
     * Table name
     */
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonProperty(value = "rls_policy")
    private RLSPolicy rlsPolicy;
//TODO    createSameTenantConstraintForForeignKey(String mainTable, String foreignKeyTable, Map<String, String> foreignKeyPrimaryKeyColumnsMappings, String constraintName)
}
