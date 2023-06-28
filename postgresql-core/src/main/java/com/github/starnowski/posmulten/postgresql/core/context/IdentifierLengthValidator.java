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
package com.github.starnowski.posmulten.postgresql.core.context;

import static java.lang.String.format;

public class IdentifierLengthValidator implements IIdentifierValidator {

    public static final int DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS = 63;

    private final int identifierMaxLength;
    private final int identifierMinLength;

    public IdentifierLengthValidator() {
        this(1, DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS);
    }

    public IdentifierLengthValidator(int identifierMinLength, int identifierMaxLength) {
        this.identifierMaxLength = identifierMaxLength;
        this.identifierMinLength = identifierMinLength;
    }

    public int getIdentifierMaxLength() {
        return identifierMaxLength;
    }

    public int getIdentifierMinLength() {
        return identifierMinLength;
    }

    @Override
    public ValidationResult validate(String identifier) {
        if (identifier != null) {
            int length = identifier.length();
            if (length >= identifierMinLength && length <= identifierMaxLength) {
                return new ValidationResult(true, "Valid");
            } else {
                return new ValidationResult(false, format("Identifier '%s' is invalid, the length must be between %d and %d", identifier, identifierMinLength, identifierMaxLength));
            }
        }
        return new ValidationResult(false, "Identifier cannot be null");
    }
}
