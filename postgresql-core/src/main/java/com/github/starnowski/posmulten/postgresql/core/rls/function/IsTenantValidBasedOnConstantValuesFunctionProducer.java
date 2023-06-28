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
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentBuilder.forType;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.IMMUTABLE;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * The component produces a statement that creates a function that checks if the passed value is the correct tenant identifier.
 * For more details about function creation please check postgres documentation
 * @see <a href="https://www.postgresql.org/docs/9.6/sql-createfunction.html">Postgres, create function</a>
 * @since 0.2
 */
public class IsTenantValidBasedOnConstantValuesFunctionProducer extends ExtendedAbstractFunctionFactory<IIsTenantValidBasedOnConstantValuesFunctionProducerParameters, IsTenantValidBasedOnConstantValuesFunctionDefinition> {
    @Override
    protected String prepareReturnType(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        return "BOOLEAN";
    }

    @Override
    protected void enrichMetadataPhraseBuilder(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withParallelModeSupplier(SAFE).withVolatilityCategorySupplier(IMMUTABLE);
    }

    @Override
    protected String buildBody(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        final String argumentType = returnFunctionArgumentType(parameters);
        sb.append(parameters.getBlacklistTenantIds().stream().sorted().map(invalidTenant ->
                format("$1 <> CAST ('%1$s' AS %2$s)", invalidTenant, argumentType)).collect(Collectors.joining(" AND ")));
        return sb.toString();
    }

    @Override
    protected IsTenantValidBasedOnConstantValuesFunctionDefinition returnFunctionDefinition(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new IsTenantValidBasedOnConstantValuesFunctionDefinition(functionDefinition);
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        return singletonList(forType(returnFunctionArgumentType(parameters)));
    }

    @Override
    protected void validate(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getBlacklistTenantIds() == null)
        {
            throw new IllegalArgumentException("The list of invalid value cannot be null");
        }
        if (parameters.getBlacklistTenantIds().isEmpty())
        {
            throw new IllegalArgumentException("The list of invalid value cannot be empty");
        }
        if (parameters.getArgumentType() != null && parameters.getArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("The argument type cannot be empty");
        }
    }

    private String returnFunctionArgumentType(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters)
    {
        return parameters.getArgumentType() == null ? "text" : parameters.getArgumentType();
    }
}
