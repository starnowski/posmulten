package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RLSPolicy {
    @NotBlank
    @JsonProperty(value = "name", required = true)
    private String name;
    @Valid
    @JsonProperty(value = "tenant_column")
    private StringWrapperWithNotBlankValue tenantColumn;
    @JsonProperty(value = "pk_columns_name_to_type")
    private Map<String, String> primaryKeyColumnsNameToTypeMap;
    @JsonProperty(value = "create_tenant_column_for_table")
    private Boolean createTenantColumnForTable;
    @NotBlank
    @JsonProperty(value = "name_for_function_that_checks_if_record_exists_in_table", required = true)
    private String nameForFunctionThatChecksIfRecordExistsInTable;
    @Valid
    @JsonProperty(value = "valid_tenant_value_constraint_name")
    private StringWrapperWithNotBlankValue validTenantValueConstraintName;
    @JsonProperty(value = "skip_adding_of_tenant_column_default_value")
    private Boolean skipAddingOfTenantColumnDefaultValue;
    public RLSPolicy setTenantColumn(String tenantColumn) {
        this.tenantColumn = new StringWrapperWithNotBlankValue(tenantColumn);
        return this;
    }

    public RLSPolicy setValidTenantValueConstraintName(String validTenantValueConstraintName) {
        this.validTenantValueConstraintName = new StringWrapperWithNotBlankValue(validTenantValueConstraintName);
        return this;
    }

    public RLSPolicy setTenantColumn(StringWrapperWithNotBlankValue tenantColumn) {
        this.tenantColumn = tenantColumn;
        return this;
    }

    public RLSPolicy setValidTenantValueConstraintName(StringWrapperWithNotBlankValue validTenantValueConstraintName) {
        this.validTenantValueConstraintName = validTenantValueConstraintName;
        return this;
    }
}
