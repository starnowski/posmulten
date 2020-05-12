package com.github.starnowski.posmulten.postgresql.core.common.function.metadata

import spock.lang.Specification

class MetadataPhraseBuilderTest extends Specification {

    def tested = new MetadataPhraseBuilder()

    def "should set supplieries interfaces" ()
    {
        given:
            VolatilityCategorySupplier volatilityCategorySupplier = { "VolatilityCategory" }
            ParallelModeSupplier parallelModeSupplier = { "ParallelMode" }

        when:
            tested.withParallelModeSupplier(parallelModeSupplier).withVolatilityCategorySupplier(volatilityCategorySupplier)

        then:
            tested.getParallelModeSupplier() == parallelModeSupplier
            tested.getVolatilityCategorySupplier() == volatilityCategorySupplier
    }
}
