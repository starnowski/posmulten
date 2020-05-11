package com.github.starnowski.posmulten.postgresql.core.common.function.metadata;

public class MetadataPhraseBuilder {

    private VolatilityCategorySupplier volatilityCategorySupplier;

    private ParallelModeSupplier parallelModeSupplier;

    public MetadataPhraseBuilder withParallelModeSupplier(ParallelModeSupplier parallelModeSupplier) {
        this.parallelModeSupplier = parallelModeSupplier;
        return this;
    }

    public ParallelModeSupplier getParallelModeSupplier() {
        return parallelModeSupplier;
    }

    public MetadataPhraseBuilder withVolatilityCategorySupplier(VolatilityCategorySupplier volatilityCategorySupplier) {
        this.volatilityCategorySupplier = volatilityCategorySupplier;
        return this;
    }

    public VolatilityCategorySupplier getVolatilityCategorySupplier() {
        return volatilityCategorySupplier;
    }
}
