package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidTenantValueConstraintConfiguration {

    @JsonProperty(value = "tenant_identifiers_blacklist", required = true)
    @Size(min = 1)
    private List<String> tenantIdentifiersBlacklist;
    @NotBlank
    @JsonProperty(value = "is_tenant_valid_function_name")
    private String isTenantValidFunctionName;
    @NotBlank
    @JsonProperty(value = "is_tenant_valid_constraint_name")
    private String isTenantValidConstraintName;
}
