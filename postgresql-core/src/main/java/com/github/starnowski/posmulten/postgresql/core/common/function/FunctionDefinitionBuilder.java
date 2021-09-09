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

import java.util.List;

public class FunctionDefinitionBuilder {

    private String createScript;

    private String functionReference;

    private String dropScript;

    private List<String> checkingStatements;

    private List<IFunctionArgument> functionArguments;

    public IFunctionDefinition build()
    {
        return new InnerFunctionDefinition(createScript, functionReference, dropScript, checkingStatements, functionArguments);
    }

    public FunctionDefinitionBuilder withCreateScript(String createScript) {
        this.createScript = createScript;
        return this;
    }

    public FunctionDefinitionBuilder withFunctionReference(String functionReference) {
        this.functionReference = functionReference;
        return this;
    }

    public FunctionDefinitionBuilder withDropScript(String dropScript) {
        this.dropScript = dropScript;
        return this;
    }

    public FunctionDefinitionBuilder withFunctionArguments(List<IFunctionArgument> functionArguments) {
        this.functionArguments = functionArguments;
        return this;
    }

    public FunctionDefinitionBuilder withCheckingStatements(List<String> checkingStatements) {
        this.checkingStatements = checkingStatements;
        return this;
    }

    private static class InnerFunctionDefinition implements IFunctionDefinition
    {
        private final String createScript;
        private final String functionReference;
        private final String dropScript;
        private final List<String> checkingStatements;
        private final List<IFunctionArgument> functionArguments;

        public InnerFunctionDefinition(String createScript, String functionReference, String dropScript, List<String> checkingStatements, List<IFunctionArgument> functionArguments) {
            this.createScript = createScript;
            this.functionReference = functionReference;
            this.dropScript = dropScript;
            this.checkingStatements = checkingStatements;
            this.functionArguments = functionArguments;
        }

        @Override
        public String getCreateScript() {
            return createScript;
        }

        @Override
        public String getFunctionReference() {
            return functionReference;
        }

        @Override
        public List<IFunctionArgument> getFunctionArguments() {
            return functionArguments;
        }

        @Override
        public List<String> getCheckingStatements() {
            return checkingStatements;
        }

        @Override
        public String getDropScript() {
            return dropScript;
        }
    }
}
