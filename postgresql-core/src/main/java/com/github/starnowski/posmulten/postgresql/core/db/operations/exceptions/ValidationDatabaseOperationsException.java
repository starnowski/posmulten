package com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationDatabaseOperationsException extends Exception {

    public Map<String, Set<String>> getFailedChecks() {
        return failedChecks;
    }

    private final Map<String, Set<String>> failedChecks;

    public ValidationDatabaseOperationsException(Map<String, Set<String>> failedChecks) {
        super(prepareMessage(failedChecks));
        this.failedChecks = failedChecks;
    }

    private static String prepareMessage(Map<String, Set<String>> failedChecks) {
        Optional<Map.Entry<String, Set<String>>> firstFailedCheck = failedChecks.entrySet().stream().findFirst();
        if (firstFailedCheck.isPresent()) {
            Map.Entry<String, Set<String>> failedCheck = firstFailedCheck.get();
            return String.format("Failed check statements for ddl instruction \"%s\", failed checks %s", failedCheck.getKey(), failedCheck.getValue().stream().map(fc -> "\"" + fc + "\"").collect(Collectors.joining(", ", "[", "]")));
        }
        return "";
    }
}