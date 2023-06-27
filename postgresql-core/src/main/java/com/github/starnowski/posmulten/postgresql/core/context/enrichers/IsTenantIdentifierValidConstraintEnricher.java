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
package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.ITableColumns;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.rls.IsTenantIdentifierValidConstraintProducer;

import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsTenantIdentifierValidConstraintProducerParameters.builder;

public class IsTenantIdentifierValidConstraintEnricher implements ISharedSchemaContextEnricher {

    private final IsTenantIdentifierValidConstraintProducer producer;

    public IsTenantIdentifierValidConstraintEnricher() {
        this(new IsTenantIdentifierValidConstraintProducer());
    }

    public IsTenantIdentifierValidConstraintEnricher(IsTenantIdentifierValidConstraintProducer producer) {
        this.producer = producer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        if (request.isConstraintForValidTenantValueShouldBeAdded()) {
            String defaultConstraintName = request.getIsTenantValidConstraintName() == null ? "tenant_identifier_valid" : request.getIsTenantValidConstraintName();
            for (Map.Entry<TableKey, ITableColumns> entry : request.getTableColumnsList().entrySet()) {
                String constraintName = request.getTenantValidConstraintCustomNamePerTables().getOrDefault(entry.getKey(), defaultConstraintName);
                String tenantColumnName = request.resolveTenantColumnByTableKey(entry.getKey());
                context.addSQLDefinition(producer.produce(builder()
                        .withConstraintName(constraintName)
                        .withTableName(entry.getKey().getTable())
                        .withTableSchema(entry.getKey().getSchema())
                        .withIIsTenantValidFunctionInvocationFactory(context.getIIsTenantValidFunctionInvocationFactory())
                        .withTenantColumnName(tenantColumnName).build()));
            }
        }
        return context;
    }
}
