/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.Objects;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum.*;

public interface FunctionArgumentValue {

    String getValue();

    FunctionArgumentValueEnum getType();

    static FunctionArgumentValue forString(String value) {
        return new DefaultFunctionArgumentValue(value, STRING);
    }

    static FunctionArgumentValue forReference(String value) {
        return new DefaultFunctionArgumentValue(value, REFERENCE);
    }

    static FunctionArgumentValue forNumeric(String value) {
        return new DefaultFunctionArgumentValue(value, NUMERIC);
    }

    final class DefaultFunctionArgumentValue implements FunctionArgumentValue {

        private final String value;

        private final FunctionArgumentValueEnum type;

        public DefaultFunctionArgumentValue(String value, FunctionArgumentValueEnum type) {
            this.value = value;
            this.type = type;
        }


        @Override
        public String getValue() {
            return value;
        }

        @Override
        public FunctionArgumentValueEnum getType() {
            return type;
        }

        @Override
        public String toString() {
            return "DefaultFunctionArgumentValue{" +
                    "value='" + value + '\'' +
                    ", type=" + type +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DefaultFunctionArgumentValue that = (DefaultFunctionArgumentValue) o;
            return Objects.equals(value, that.value) &&
                    type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, type);
        }

    }
}
