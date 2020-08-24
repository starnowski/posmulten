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

import com.github.starnowski.posmulten.postgresql.core.*;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

import java.util.ArrayList;
import java.util.List;

public class SingleTenantColumnSQLDefinitionsProducer {

    private CreateColumnStatementProducer createColumnStatementProducer = new CreateColumnStatementProducer();
    private SetDefaultStatementProducer setDefaultStatementProducer = new SetDefaultStatementProducer();
    private SetNotNullStatementProducer setNotNullStatementProducer = new SetNotNullStatementProducer();

    public List<SQLDefinition> produce(TableKey tableKey, AbstractTableColumns tableColumns, String defaultTenantColumnValue, String defaultTenantColumn, String defaultTenantColumnType)
    {
        List<SQLDefinition> results = new ArrayList<>();
        String tenantColumn = tableColumns.getTenantColumnName() == null ? defaultTenantColumn : tableColumns.getTenantColumnName();
        results.add(createColumnStatementProducer.produce(new CreateColumnStatementProducerParameters(tableKey.getTable(), tenantColumn, defaultTenantColumnType, tableKey.getSchema())));
        results.add(setDefaultStatementProducer.produce(new SetDefaultStatementProducerParameters(tableKey.getTable(), tenantColumn, defaultTenantColumnValue, tableKey.getSchema())));
        results.add(setNotNullStatementProducer.produce(new SetNotNullStatementProducerParameters(tableKey.getTable(), tenantColumn, tableKey.getSchema())));
        return results;
    }

    public void setCreateColumnStatementProducer(CreateColumnStatementProducer createColumnStatementProducer) {
        this.createColumnStatementProducer = createColumnStatementProducer;
    }

    public void setSetDefaultStatementProducer(SetDefaultStatementProducer setDefaultStatementProducer) {
        this.setDefaultStatementProducer = setDefaultStatementProducer;
    }

    public void setSetNotNullStatementProducer(SetNotNullStatementProducer setNotNullStatementProducer) {
        this.setNotNullStatementProducer = setNotNullStatementProducer;
    }

}
