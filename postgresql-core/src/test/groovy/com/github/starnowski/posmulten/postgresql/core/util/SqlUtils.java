package com.github.starnowski.posmulten.postgresql.core.util;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SqlUtils {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void assertAllCheckingStatementsArePassing(List<String> selectStatements) {
        Map<String, Long> resultsMap = selectAndReturnMapOfStatementsAndItResultsForListOfSelectStatements(jdbcTemplate, selectStatements);
        for (Map.Entry<String, Long> entry : resultsMap.entrySet()) {
            Assert.assertTrue(String.format("Result was lower or equal to zero for statement %s", entry.getKey()), entry.getValue() > 0);
        }
    }

    public static Map<String, Long> selectAndReturnMapOfStatementsAndItResultsForListOfSelectStatements(JdbcTemplate jdbcTemplate, List<String> selectStatements) {
        return selectStatements.stream().map(selectStatement -> {
                    Long result = jdbcTemplate.execute((StatementCallback<Long>) statement -> {
                        ResultSet rs = statement.executeQuery(selectStatement);
                        rs.next();
                        return rs.getLong(1);
                    });
                    return new Pair<>(selectStatement, result);
                }
        ).collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));
    }
}
