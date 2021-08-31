package com.github.starnowski.posmulten.postgresql.core.util;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
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

    public void assertAllResultForCheckingStatementsAreEqualZero(IFunctionDefinition functionDefinition) {
        assertAllResultForCheckingStatementsAreEqualZero(functionDefinition.getCheckingStatements());
    }

    public void assertAllResultForCheckingStatementsAreEqualZero(List<String> selectStatements) {
        Map<String, Long> resultsMap = selectAndReturnMapOfStatementsAndItResultsForListOfSelectStatements(jdbcTemplate, selectStatements);
        for (Map.Entry<String, Long> entry : resultsMap.entrySet()) {
            Assert.assertTrue(String.format("Result was not equal to zero for statement %s", entry.getKey()), entry.getValue() == 0);
        }
    }

    public void assertAllCheckingStatementsArePassing(IFunctionDefinition functionDefinition) {
        assertAllCheckingStatementsArePassing(functionDefinition.getCheckingStatements());
    }

    public void assertAllCheckingStatementsArePassing(List<String> selectStatements) {
        Map<String, Long> resultsMap = selectAndReturnMapOfStatementsAndItResultsForListOfSelectStatements(jdbcTemplate, selectStatements);
        for (Map.Entry<String, Long> entry : resultsMap.entrySet()) {
            Assert.assertTrue(String.format("Result was lower or equal to zero for statement %s", entry.getKey()), entry.getValue() > 0);
        }
    }
}
