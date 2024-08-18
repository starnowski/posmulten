package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractRLSPolicy
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry

import static java.util.Arrays.asList

abstract class AbstractTableEntryMapperTest <FKC extends AbstractForeignKeyConfiguration, RLSP extends AbstractRLSPolicy, T extends AbstractTableEntry, M extends IConfigurationMapper< TableEntry, T>, CMTC extends AbstractConfigurationMapperTestContext> extends AbstractConfigurationMapperTest<T, TableEntry, M, CMTC> {
    @Override
    protected Class<TableEntry> getConfigurationObjectClass() {
        TableEntry.class
    }
    protected abstract T createOutputInstance()
    protected abstract FKC createForeignKeyConfigurationInstance()
    protected abstract RLSP createRLSPolicyInstance()

    @Override
    protected List<T> prepareExpectedMappedObjectsList() {
        [
                createOutputInstance(),
                createOutputInstance().setName("table_1"),
                createOutputInstance().setName("users_t").setRlsPolicy(createRLSPolicyInstance()),
                createOutputInstance().setName("users_t").setForeignKeys(new ArrayList<FKC>()),
                createOutputInstance().setName("users_t").setForeignKeys(asList(createForeignKeyConfigurationInstance().setTableName("tabXXX"))),
                createOutputInstance().setName("posts").setRlsPolicy(createRLSPolicyInstance().setName("rls_users_policy"))
                        .setForeignKeys(asList(createForeignKeyConfigurationInstance().setTableName("tabXXX"), createForeignKeyConfigurationInstance().setTableName("comments"))),
        ]
    }

    @Override
    protected List<TableEntry> prepareExpectedUnmappeddObjectsList() {
        [
                new TableEntry(),
                new TableEntry().setName("table_1"),
                new TableEntry().setName("users_t").setRlsPolicy(new RLSPolicy()),
                new TableEntry().setName("users_t").setForeignKeys(new ArrayList<ForeignKeyConfiguration>()),
                new TableEntry().setName("users_t").setForeignKeys(asList(new ForeignKeyConfiguration().setTableName("tabXXX"))),
                new TableEntry().setName("posts").setRlsPolicy(new RLSPolicy().setName("rls_users_policy"))
                        .setForeignKeys(asList(new ForeignKeyConfiguration().setTableName("tabXXX"), new ForeignKeyConfiguration().setTableName("comments"))),
        ]
    }
}
