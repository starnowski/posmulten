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
package com.github.starnowski.posmulten.configuration.yaml.jakarta.validation;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, String> {

    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(EnumNamePattern constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        if (Stream.of(this.enumClass.getEnumConstants()).map(e -> ((Enum) e).name()).noneMatch(name -> name.equals(value))) {
            HibernateConstraintValidatorContext hibernateContext = constraintValidatorContext.unwrap(
                    HibernateConstraintValidatorContext.class );
            hibernateContext.addExpressionVariable("enumValues", Stream.of(this.enumClass.getEnumConstants()).map(e -> ((Enum) e).name()).collect(Collectors.joining(", ")));
            return false;
        }
        return true;
    }
}
