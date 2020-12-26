package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidTenantValueConstraintConfiguration {

    @JsonProperty(value = "tenant_identifiers_blacklist", required = true)
    //TODO Not empty
    private List<String> tenantIdentifiersBlacklist;
    @JsonProperty(value = "is_tenant_valid_function_name")
    private String isTenantValidFunctionName;
    @JsonProperty(value = "is_tenant_valid_constraint_name")
    private String isTenantValidConstraintName;
}
