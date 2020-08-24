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

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.EnableRowLevelSecurityProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.ForceRowLevelSecurityProducer;

import java.util.ArrayList;
import java.util.List;

public class TableRLSSettingsSQLDefinitionsProducer {

    private EnableRowLevelSecurityProducer enableRowLevelSecurityProducer = new EnableRowLevelSecurityProducer();
    private ForceRowLevelSecurityProducer forceRowLevelSecurityProducer = new ForceRowLevelSecurityProducer();

    public List<SQLDefinition> produce(TableKey tableKey, boolean forceRowLevelSecurity)
    {
        List<SQLDefinition> results = new ArrayList<>();
        results.add(enableRowLevelSecurityProducer.produce(tableKey.getTable(), tableKey.getSchema()));
        if (forceRowLevelSecurity)
        {
            results.add(forceRowLevelSecurityProducer.produce(tableKey.getTable(), tableKey.getSchema()));
        }
        return results;
    }

    void setEnableRowLevelSecurityProducer(EnableRowLevelSecurityProducer enableRowLevelSecurityProducer) {
        this.enableRowLevelSecurityProducer = enableRowLevelSecurityProducer;
    }

    void setForceRowLevelSecurityProducer(ForceRowLevelSecurityProducer forceRowLevelSecurityProducer) {
        this.forceRowLevelSecurityProducer = forceRowLevelSecurityProducer;
    }
}
