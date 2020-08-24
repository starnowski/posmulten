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
package com.github.starnowski.posmulten.postgresql.core.common.function;

import java.util.Objects;

public class FunctionArgumentBuilder {

    private String type;

    public static IFunctionArgument forType(String type)
    {
        return new FunctionArgumentBuilder().withType(type).build();
    }

    public IFunctionArgument build()
    {
        return new InnerFunctionArgument(type);
    }

    public FunctionArgumentBuilder withType(String type) {
        this.type = type;
        return this;
    }

    private static class InnerFunctionArgument implements IFunctionArgument
    {
        public InnerFunctionArgument(String type) {
            this.type = type;
        }

        private final String type;

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "InnerFunctionArgument{" +
                    "type='" + type + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerFunctionArgument that = (InnerFunctionArgument) o;
            return Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
