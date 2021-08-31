package com.github.starnowski.posmulten.postgresql.core.util;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.selectAndReturnMapOfStatementsAndItResultsForListOfSelectStatements;

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
}
