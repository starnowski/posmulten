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
package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentBuilder;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.STABLE;

public class TenantHasAuthoritiesFunctionProducer extends ExtendedAbstractFunctionFactory<ITenantHasAuthoritiesFunctionProducerParameters, TenantHasAuthoritiesFunctionDefinition> {

    public static final String DEFAULT_ARGUMENT_TYPE = "VARCHAR(255)";

    @Override
    protected TenantHasAuthoritiesFunctionDefinition returnFunctionDefinition(ITenantHasAuthoritiesFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new TenantHasAuthoritiesFunctionDefinition(functionDefinition);
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        return Arrays.asList(argument(parameters.getTenantIdArgumentType()), argument(parameters.getPermissionCommandPolicyArgumentType()), argument(parameters.getRLSExpressionArgumentType())
        ,argument(parameters.getTableArgumentType()), argument(parameters.getSchemaArgumentType()));
    }

    private IFunctionArgument argument(String type)
    {
        return FunctionArgumentBuilder.forType(type == null ? DEFAULT_ARGUMENT_TYPE : type);
    }

    @Override
    protected String prepareReturnType(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        return "BOOLEAN";
    }

    @Override
    protected void enrichMetadataPhraseBuilder(ITenantHasAuthoritiesFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withVolatilityCategorySupplier(STABLE).withParallelModeSupplier(SAFE);
    }

    @Override
    protected void validate(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getEqualsCurrentTenantIdentifierFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("Parameter of type EqualsCurrentTenantIdentifierFunctionInvocationFactory cannot be null");
        }
        if (parameters.getTenantIdArgumentType() != null && parameters.getTenantIdArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Tenant Id type cannot be blank");
        }
        if (parameters.getPermissionCommandPolicyArgumentType() != null && parameters.getPermissionCommandPolicyArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Permission command policy argument type cannot be blank");
        }
        if (parameters.getRLSExpressionArgumentType() != null && parameters.getRLSExpressionArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("RLS expression argument type cannot be blank");
        }
        if (parameters.getTableArgumentType() != null && parameters.getTableArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Table argument type cannot be blank");
        }
        if (parameters.getSchemaArgumentType() != null && parameters.getSchemaArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Schema argument type cannot be blank");
        }
    }

    @Override
    protected String buildBody(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(parameters.getEqualsCurrentTenantIdentifierFunctionInvocationFactory().returnEqualsCurrentTenantIdentifierFunctionInvocation(forReference("$1")));
        return sb.toString();
    }
}
