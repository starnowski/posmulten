package com.github.starnowski.posmulten.postgresql.core.context.comparable;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

public interface SharedSchemaContextComparator {


    SharedSchemaContextComparableResults diff(ISharedSchemaContext left, ISharedSchemaContext right);

    class SharedSchemaContextComparableResults {

        private final ComparableResult creationScriptsDifferences;
        private final ComparableResult dropScriptsDifferences;
        private final ComparableResult checkScriptsDifferences;
        public SharedSchemaContextComparableResults(ComparableResult creationScriptsDifferences, ComparableResult dropScriptsDifferences, ComparableResult checkScriptsDifferences) {
            this.creationScriptsDifferences = creationScriptsDifferences;
            this.dropScriptsDifferences = dropScriptsDifferences;
            this.checkScriptsDifferences = checkScriptsDifferences;
        }

        public ComparableResult getCreationScriptsDifferences() {
            return creationScriptsDifferences;
        }

        public ComparableResult getDropScriptsDifferences() {
            return dropScriptsDifferences;
        }

        public ComparableResult getCheckScriptsDifferences() {
            return checkScriptsDifferences;
        }
    }

    class ComparableResult {
        private final List<String> existedOnlyOnLeft;
        private final List<String> existedOnlyOnRight;

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
    }
}
