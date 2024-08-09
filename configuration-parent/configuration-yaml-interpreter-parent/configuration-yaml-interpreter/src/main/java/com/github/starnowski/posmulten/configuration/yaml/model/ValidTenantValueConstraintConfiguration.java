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
