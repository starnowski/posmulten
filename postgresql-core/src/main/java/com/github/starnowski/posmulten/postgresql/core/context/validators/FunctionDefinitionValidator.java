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
