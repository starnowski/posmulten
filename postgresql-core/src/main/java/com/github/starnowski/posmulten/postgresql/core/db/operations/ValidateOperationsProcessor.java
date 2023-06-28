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
package com.github.starnowski.posmulten.postgresql.core.db.operations;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException;
import com.github.starnowski.posmulten.postgresql.core.db.operations.util.SQLUtil;
import com.github.starnowski.posmulten.postgresql.core.util.Pair;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidateOperationsProcessor implements IDatabaseOperationsProcessor {

    private final SQLUtil sqlUtil;

    public ValidateOperationsProcessor() {
        this(new SQLUtil());
    }

    ValidateOperationsProcessor(SQLUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }

    @Override
    public void run(DataSource dataSource, List<SQLDefinition> sqlDefinitions) throws SQLException, ValidationDatabaseOperationsException {
        Map<String, Set<String>> failedChecks = null;
        try (Connection connection = dataSource.getConnection()) {
            failedChecks = sqlDefinitions.stream().flatMap(definition -> definition.getCheckingStatements().stream().map(cs -> new Pair<String, String>(definition.getCreateScript(), cs)))
                    .filter(csKey -> {
                                try {
                                    long result = sqlUtil.returnLongResultForQuery(connection, csKey.getValue());
                                    return result <= 0;
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
                    .collect(Collectors.toMap(cs1 -> cs1.getKey(), cs2 -> new HashSet<String>(Collections.singletonList(cs2.getValue())), (o1, o2) -> new HashSet<String>(Stream.concat(o1.stream(), o2.stream()).collect(Collectors.toSet())), () -> new LinkedHashMap<String, Set<String>>()));
        }
        if (!failedChecks.isEmpty()) {
            throw new ValidationDatabaseOperationsException(failedChecks);
        }
    }

    SQLUtil getSqlUtil() {
        return sqlUtil;
    }
}
