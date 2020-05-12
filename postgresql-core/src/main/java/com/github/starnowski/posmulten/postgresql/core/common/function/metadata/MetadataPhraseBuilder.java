package com.github.starnowski.posmulten.postgresql.core.common.function.metadata;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class MetadataPhraseBuilder {

    private VolatilityCategorySupplier volatilityCategorySupplier;

    private ParallelModeSupplier parallelModeSupplier;

    public String build()
    {
        List<String> metadataDefinitions = new ArrayList<>();
        if (volatilityCategorySupplier != null)
        {
            metadataDefinitions.add(volatilityCategorySupplier.getVolatilityCategoryString());
        }
        if (parallelModeSupplier != null)
        {
            metadataDefinitions.add(parallelModeSupplier.getParallelModeString());
        }
        return metadataDefinitions.stream().collect(joining("\n"));
    }

    public MetadataPhraseBuilder withParallelModeSupplier(ParallelModeSupplier parallelModeSupplier) {
        this.parallelModeSupplier = parallelModeSupplier;
        return this;
    }

    public MetadataPhraseBuilder withVolatilityCategorySupplier(VolatilityCategorySupplier volatilityCategorySupplier) {
        this.volatilityCategorySupplier = volatilityCategorySupplier;
        return this;
    }
}
