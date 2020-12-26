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
public class SharedSchemaContextConfiguration {

    @JsonProperty(value = "default_schema", required = true)
    private String defaultSchema;
    @JsonProperty(value = "current_tenant_id_property_type")
    private String currentTenantIdPropertyType;
    @JsonProperty(value = "current_tenant_id_property")
    private String currentTenantIdProperty;
    @JsonProperty(value = "get_current_tenant_id_function_name")
    private String getCurrentTenantIdFunctionName;
    @JsonProperty(value = "set_current_tenant_id_function_name")
    private String setCurrentTenantIdFunctionName;
    @JsonProperty(value = "equals_current_tenant_identifier_function_name")
    private String equalsCurrentTenantIdentifierFunctionName;
    @JsonProperty(value = "tenant_has_authorities_function_name")
    private String tenantHasAuthoritiesFunctionName;
    @JsonProperty(value = "force_row_level_security_for_table_owner")
    private Boolean forceRowLevelSecurityForTableOwner;
    @JsonProperty(value = "default_tenant_id_column")
    private String defaultTenantIdColumn;
    @JsonProperty(value = "grantee", required = true)
    private String grantee;
    @JsonProperty(value = "set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables")
    private Boolean currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables;
    @JsonProperty(value = "valid_tenant_value_constraint")
    private ValidTenantValueConstraintConfiguration validTenantValueConstraint;
}
