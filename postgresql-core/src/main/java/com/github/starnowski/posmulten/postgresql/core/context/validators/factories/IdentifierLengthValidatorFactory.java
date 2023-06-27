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
package com.github.starnowski.posmulten.postgresql.core.context.validators.factories;

import com.github.starnowski.posmulten.postgresql.core.context.IdentifierLengthValidator;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;

import static com.github.starnowski.posmulten.postgresql.core.context.IdentifierLengthValidator.DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS;

public class IdentifierLengthValidatorFactory implements IIdentifierValidatorFactory<IdentifierLengthValidator> {

    @Override
    public IdentifierLengthValidator build(SharedSchemaContextRequest sharedSchemaContextRequest) throws
            InvalidSharedSchemaContextRequestException {
        int identifierMinLength;
        int identifierMaxLength;
        if (sharedSchemaContextRequest.getIdentifierMinLength() != null) {
            if (sharedSchemaContextRequest.getIdentifierMinLength() <= 0) {
                throw new InvalidSharedSchemaContextRequestException("The identifierMinLength property value can not be less or equal to zero");
            }
            identifierMinLength = sharedSchemaContextRequest.getIdentifierMinLength();
        } else {
            identifierMinLength = 1;
        }
        if (sharedSchemaContextRequest.getIdentifierMaxLength() != null) {
            if (sharedSchemaContextRequest.getIdentifierMaxLength() <= 0) {
                throw new InvalidSharedSchemaContextRequestException("The identifierMaxLength property value can not be less or equal to zero");
            }
            identifierMaxLength = sharedSchemaContextRequest.getIdentifierMaxLength();
        } else {
            identifierMaxLength = DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS;
        }
        return new IdentifierLengthValidator(identifierMinLength, identifierMaxLength);
    }
}
