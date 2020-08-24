/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
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

    public VolatilityCategorySupplier getVolatilityCategorySupplier() {
        return volatilityCategorySupplier;
    }

    public ParallelModeSupplier getParallelModeSupplier() {
        return parallelModeSupplier;
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
