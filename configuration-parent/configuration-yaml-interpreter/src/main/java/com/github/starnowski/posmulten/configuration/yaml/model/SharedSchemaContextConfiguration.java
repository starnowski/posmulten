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
public class SharedSchemaContextConfiguration {

    @NotBlank
    @JsonProperty(value = "default_schema", required = true)
    private String defaultSchema;
    @NotBlank
    @JsonProperty(value = "current_tenant_id_property_type")
    private String currentTenantIdPropertyType;
    @NotBlank
    @JsonProperty(value = "current_tenant_id_property")
    private String currentTenantIdProperty;
    @NotBlank
    @JsonProperty(value = "get_current_tenant_id_function_name")
    private String getCurrentTenantIdFunctionName;
    @NotBlank
    @JsonProperty(value = "set_current_tenant_id_function_name")
    private String setCurrentTenantIdFunctionName;
    @NotBlank
    @JsonProperty(value = "equals_current_tenant_identifier_function_name")
    private String equalsCurrentTenantIdentifierFunctionName;
    @NotBlank
    @JsonProperty(value = "tenant_has_authorities_function_name")
    private String tenantHasAuthoritiesFunctionName;
    @JsonProperty(value = "force_row_level_security_for_table_owner")
    private Boolean forceRowLevelSecurityForTableOwner;
    @NotBlank
    @JsonProperty(value = "default_tenant_id_column")
    private String defaultTenantIdColumn;
    @NotBlank
    @JsonProperty(value = "grantee", required = true)
    private String grantee;
    @JsonProperty(value = "set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables")
    private Boolean currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables;
    @JsonProperty(value = "valid_tenant_value_constraint")
    private ValidTenantValueConstraintConfiguration validTenantValueConstraint;
    @JsonProperty(value = "tables")
    private List<TableEntry> tables;
}
