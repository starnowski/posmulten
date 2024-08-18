/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posmulten.configuration.yaml.jakarta.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractRLSPolicy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RLSPolicy implements AbstractRLSPolicy<StringWrapperWithNotBlankValue, PrimaryKeyDefinition, RLSPolicy> {
    @NotBlank
    @JsonProperty(value = "name", required = true)
    private String name;
    @Valid
    @JsonProperty(value = "tenant_column")
    private StringWrapperWithNotBlankValue tenantColumn;
    @JsonProperty(value = "create_tenant_column_for_table")
    private Boolean createTenantColumnForTable;
    @Valid
    @JsonProperty(value = "valid_tenant_value_constraint_name")
    private StringWrapperWithNotBlankValue validTenantValueConstraintName;
    @JsonProperty(value = "skip_adding_of_tenant_column_default_value")
    private Boolean skipAddingOfTenantColumnDefaultValue;
    @Valid
    @JsonProperty(value = "primary_key_definition")
    private PrimaryKeyDefinition primaryKeyDefinition;
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
