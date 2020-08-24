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
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.VOLATILE;
import static java.util.Collections.singletonList;

/**
 * The component produces a statement that creates a function that sets the current tenant identifier.
 * For more details about function creation please check postgres documentation
 * @see <a href="https://www.postgresql.org/docs/9.6/sql-createfunction.html">Postgres, create function</a>
 *
 */
public class SetCurrentTenantIdFunctionProducer extends ExtendedAbstractFunctionFactory<ISetCurrentTenantIdFunctionProducerParameters, SetCurrentTenantIdFunctionDefinition> {

    @Override
    protected void validate(ISetCurrentTenantIdFunctionProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getCurrentTenantIdProperty() == null)
        {
            throw new IllegalArgumentException("Tenant id property name cannot be null");
        }
        if (parameters.getCurrentTenantIdProperty().trim().isEmpty())
        {
            throw new IllegalArgumentException("Tenant id property name cannot be blank");
        }
        if (parameters.getArgumentType() != null && parameters.getArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Argument type cannot be blank");
        }
    }

    @Override
    protected SetCurrentTenantIdFunctionDefinition returnFunctionDefinition(ISetCurrentTenantIdFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new SetCurrentTenantIdFunctionDefinition(functionDefinition);
    }

    @Override
    protected String prepareReturnType(ISetCurrentTenantIdFunctionProducerParameters parameters) {
        return "VOID";
    }

    @Override
    protected void enrichMetadataPhraseBuilder(ISetCurrentTenantIdFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withVolatilityCategorySupplier(VOLATILE);
    }

    @Override
    protected String buildBody(ISetCurrentTenantIdFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN");
        sb.append("\n");
        sb.append("PERFORM set_config('");
        sb.append(parameters.getCurrentTenantIdProperty());
        sb.append("', $1, false);");
        sb.append("\n");
        sb.append("END");
        return sb.toString();
    }

    @Override
    protected String returnFunctionLanguage(ISetCurrentTenantIdFunctionProducerParameters parameters) {
        return "plpgsql";
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(ISetCurrentTenantIdFunctionProducerParameters parameters) {
        return singletonList(forType(parameters.getArgumentType() == null ? "text" : parameters.getArgumentType()));
    }
}
