package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RLSPolicy {
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonProperty(value = "tenant_column")
    private String tenantColumn;
    @JsonProperty(value = "pk_columns_name_to_type")
    private Map<String, String> primaryKeyColumnsNameToTypeMap;
    @JsonProperty(value = "create_tenant_column_for_table")
    private Boolean createTenantColumnForTable;
    @JsonProperty(value = "name_for_function_that_checks_if_record_exists_in_table", required = true)
    private String nameForFunctionThatChecksIfRecordExistsInTable;
    @JsonProperty(value = "valid_tenant_value_constraint_name")
    private String validTenantValueConstraintName;
    @JsonProperty(value = "skip_adding_of_tenant_column_default_value")
    private Boolean skipAddingOfTenantColumnDefaultValue;
}
