package com.github.starnowski.posmulten.postgresql.core.context.comparable;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.ArrayList;
import java.util.List;

public interface SharedSchemaContextComparator {


    SharedSchemaContextComparableResults compare(ISharedSchemaContext left, ISharedSchemaContext right);

    class SharedSchemaContextComparableResults {

        public SharedSchemaContextComparableResults(ComparableResult creationScriptsDifferences, ComparableResult deletionScriptsDifferences, ComparableResult checkScriptsDifferences) {
            this.creationScriptsDifferences = creationScriptsDifferences;
            this.deletionScriptsDifferences = deletionScriptsDifferences;
            this.checkScriptsDifferences = checkScriptsDifferences;
        }

        private ComparableResult creationScriptsDifferences;
        private ComparableResult deletionScriptsDifferences;
        private ComparableResult checkScriptsDifferences;

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

        private List<String> existedOnlyOnLeft = new ArrayList<>();
        private List<String> existedOnlyOnRight = new ArrayList<>();
    }
}
