package com.github.starnowski.posmulten.postgresql.core.context.comparable;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

public interface SharedSchemaContextComparator {


    SharedSchemaContextComparableResults compare(ISharedSchemaContext left, ISharedSchemaContext right);

    class SharedSchemaContextComparableResults {

        public SharedSchemaContextComparableResults(ComparableResult creationScriptsDifferences, ComparableResult deletionScriptsDifferences, ComparableResult checkScriptsDifferences) {
            this.creationScriptsDifferences = creationScriptsDifferences;
            this.deletionScriptsDifferences = deletionScriptsDifferences;
            this.checkScriptsDifferences = checkScriptsDifferences;
        }

        private final ComparableResult creationScriptsDifferences;
        private final ComparableResult deletionScriptsDifferences;
        private final ComparableResult checkScriptsDifferences;

        public ComparableResult getCreationScriptsDifferences() {
            return creationScriptsDifferences;
        }

        public ComparableResult getDeletionScriptsDifferences() {
            return deletionScriptsDifferences;
        }

        public ComparableResult getCheckScriptsDifferences() {
            return checkScriptsDifferences;
        }
    }

    class ComparableResult {
        public ComparableResult(List<String> existedOnlyOnLeft, List<String> existedOnlyOnRight) {
            this.existedOnlyOnLeft = unmodifiableList(ofNullable(existedOnlyOnLeft).orElse(new ArrayList<>()));
            this.existedOnlyOnRight = unmodifiableList(ofNullable(existedOnlyOnRight).orElse(new ArrayList<>()));
        }

        public List<String> getExistedOnlyOnLeft() {
            return existedOnlyOnLeft;
        }

        public List<String> getExistedOnlyOnRight() {
            return existedOnlyOnRight;
        }

        private final List<String> existedOnlyOnLeft;
        private final List<String> existedOnlyOnRight;
    }
}
