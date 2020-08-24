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

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentBuilder.forType;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.STABLE;
import static java.util.Collections.singletonList;

/**
 * The component produces a statement that creates a function that returns boolean which determines if passed tenant id belongs to the current tenant.
 * For more details about function creation please check postgres documentation
 * @see <a href="https://www.postgresql.org/docs/9.6/sql-createfunction.html">Postgres, create function</a>
 *
 */
public class EqualsCurrentTenantIdentifierFunctionProducer extends ExtendedAbstractFunctionFactory<IEqualsCurrentTenantIdentifierFunctionProducerParameters, EqualsCurrentTenantIdentifierFunctionDefinition> {

    public static final String DEFAULT_ARGUMENT_TYPE = "VARCHAR(255)";

    @Override
    protected EqualsCurrentTenantIdentifierFunctionDefinition returnFunctionDefinition(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new EqualsCurrentTenantIdentifierFunctionDefinition(functionDefinition);
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        return singletonList(forType(parameters.getArgumentType() == null ? DEFAULT_ARGUMENT_TYPE : parameters.getArgumentType()));
    }

    @Override
    protected String prepareReturnType(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        return "BOOLEAN";
    }

    @Override
    protected void enrichMetadataPhraseBuilder(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withVolatilityCategorySupplier(STABLE).withParallelModeSupplier(SAFE);
    }

    @Override
    protected String buildBody(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT $1 = ");
        sb.append(parameters.getCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation());
        return sb.toString();
    }

    @Override
    protected void validate(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getCurrentTenantIdFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("Parameter of type IGetCurrentTenantIdFunctionInvocationFactory cannot be null");
        }
        if (parameters.getArgumentType() != null && parameters.getArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Argument type cannot be blank");
        }
    }
}
