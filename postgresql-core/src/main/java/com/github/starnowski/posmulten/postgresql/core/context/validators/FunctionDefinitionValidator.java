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
package com.github.starnowski.posmulten.postgresql.core.context.validators;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.IIdentifierValidator;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidIdentifierException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;

public class FunctionDefinitionValidator implements ISQLDefinitionsValidator {

    private final List<IIdentifierValidator> iIdentifierValidators;

    public FunctionDefinitionValidator(List<IIdentifierValidator> iIdentifierValidators) {
        this.iIdentifierValidators = iIdentifierValidators;
    }

    @Override
    public void validate(List<SQLDefinition> sqlDefinitions) throws SharedSchemaContextBuilderException {
        if (sqlDefinitions == null || this.iIdentifierValidators == null) {
            return;
        }
        boolean valid = true;
        List<String> exceptionMessages = new ArrayList<>();
        for (SQLDefinition definition : sqlDefinitions) {
            if (definition instanceof IFunctionDefinition) {
                String functionReference = ((IFunctionDefinition) definition).getFunctionReference();
                String functionName = !functionReference.contains(".") ? functionReference : functionReference.indexOf(".") + 1 == functionReference.length() ? "" : functionReference.substring(functionReference.indexOf(".") + 1);
                boolean isFunctionNameValid = true;
                StringBuilder exceptionPartMessageBuilder = new StringBuilder();
                exceptionPartMessageBuilder.append("Invalid identifier for function ");
                exceptionPartMessageBuilder.append(functionReference);
                exceptionPartMessageBuilder.append(": ");
                for (IIdentifierValidator identifierValidator : iIdentifierValidators) {
                    IIdentifierValidator.ValidationResult result = identifierValidator.validate(functionName);
                    valid &= result.isValid();
                    isFunctionNameValid &= result.isValid();
                    if (!result.isValid()) {
                        exceptionPartMessageBuilder.append(result.getMessage());
                    }
                }
                if (!isFunctionNameValid) {
                    exceptionMessages.add(exceptionPartMessageBuilder.toString());
                }
            }
        }
        if (!valid) {
            throw new InvalidIdentifierException(join(",", exceptionMessages));
        }
    }
}
