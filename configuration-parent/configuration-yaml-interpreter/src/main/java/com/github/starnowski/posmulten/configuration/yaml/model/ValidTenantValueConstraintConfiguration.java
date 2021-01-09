package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @Size(min = 1, message = "must have at least one element")
    @NotNull
    private List<String> tenantIdentifiersBlacklist;
    @Valid
    @JsonProperty(value = "is_tenant_valid_function_name")
    private StringWrapperWithNotBlankValue isTenantValidFunctionName;
    @Valid
    @JsonProperty(value = "is_tenant_valid_constraint_name")
    private StringWrapperWithNotBlankValue isTenantValidConstraintName;
    public ValidTenantValueConstraintConfiguration setIsTenantValidFunctionName(String isTenantValidFunctionName) {
        this.isTenantValidFunctionName = new StringWrapperWithNotBlankValue(isTenantValidFunctionName);
        return this;
    }

    public ValidTenantValueConstraintConfiguration setIsTenantValidConstraintName(String isTenantValidConstraintName) {
        this.isTenantValidConstraintName = new StringWrapperWithNotBlankValue(isTenantValidConstraintName);
        return this;
    }

    public ValidTenantValueConstraintConfiguration setIsTenantValidFunctionName(StringWrapperWithNotBlankValue isTenantValidFunctionName) {
        this.isTenantValidFunctionName = isTenantValidFunctionName;
        return this;
    }

    public ValidTenantValueConstraintConfiguration setIsTenantValidConstraintName(StringWrapperWithNotBlankValue isTenantValidConstraintName) {
        this.isTenantValidConstraintName = isTenantValidConstraintName;
        return this;
    }
}
