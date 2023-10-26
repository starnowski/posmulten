package com.github.starnowski.posmulten.postgresql.core.context.comparable;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.ArrayList;
import java.util.List;

public interface SharedSchemaContextComparator {


    SharedSchemaContextComparableResults compare(ISharedSchemaContext left, ISharedSchemaContext right);

    class SharedSchemaContextComparableResults {

        private ComparableResult creationScriptsDifferences;
        private ComparableResult deletionScriptsDifferences;
        private ComparableResult checkScriptsDifferences;
    }

    class ComparableResult {

        private List<String> existedOnlyOnLeft = new ArrayList<>();
        private List<String> existedOnlyOnRight = new ArrayList<>();
    }
}
