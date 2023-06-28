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
package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Comparator.comparingInt;

public class DefaultSharedSchemaContextBuilderFactorySupplierResolver {

    private final FileExtensionExtractor fileExtensionExtractor = new FileExtensionExtractor();

    public IDefaultSharedSchemaContextBuilderFactorySupplier resolveSupplierBasedOnPriorityForFile(String file, Set<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers) {
        String fileExtension = fileExtensionExtractor.extract(file);
        Comparator<IDefaultSharedSchemaContextBuilderFactorySupplier> priorityComparator = comparingInt(IDefaultSharedSchemaContextBuilderFactorySupplier::getPriority);
        return suppliers.stream().filter(supplier -> extensionIsSupportedBySupplier(fileExtension, supplier.getSupportedFileExtensions())).max(priorityComparator).orElse(null);
    }

    private boolean extensionIsSupportedBySupplier(String fileExtension, List<String> supportedFileExtensions)
    {
        return supportedFileExtensions == null ? false : supportedFileExtensions.stream().anyMatch(supportedFileExtension -> extensionIsEqual(fileExtension, supportedFileExtension));
    }

    private boolean extensionIsEqual(String fileExtension, String supportedExtension)
    {
        fileExtension = fileExtension == null ? null : fileExtension.toLowerCase();
        supportedExtension = supportedExtension == null ? null : supportedExtension.toLowerCase();
        return Objects.equals(fileExtension, supportedExtension);
    }
}
