package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy

import static java.util.Arrays.asList

class TableEntryMapperTest extends AbstractConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.TableEntry, com.github.starnowski.posmulten.configuration.core.model.TableEntry, TableEntryMapper> {
    @Override
    protected Class<TableEntry> getConfigurationObjectClass() {
        TableEntry.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.TableEntry> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.TableEntry.class
    }

    @Override
    protected TableEntryMapper getTestedObject() {
        new TableEntryMapper()
    }

    @Override
    protected List<com.github.starnowski.posmulten.configuration.yaml.model.TableEntry> prepareExpectedMappedObjectsList() {
        [
                new com.github.starnowski.posmulten.configuration.yaml.model.TableEntry(),
                new com.github.starnowski.posmulten.configuration.yaml.model.TableEntry().setName("table_1"),
                new com.github.starnowski.posmulten.configuration.yaml.model.TableEntry().setName("users_t").setRlsPolicy(new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy()),
                new com.github.starnowski.posmulten.configuration.yaml.model.TableEntry().setName("users_t").setForeignKeys(new ArrayList<com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration>()),
                new com.github.starnowski.posmulten.configuration.yaml.model.TableEntry().setName("users_t").setForeignKeys(asList(new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration().setTableName("tabXXX"))),
                new com.github.starnowski.posmulten.configuration.yaml.model.TableEntry().setName("posts").setRlsPolicy(new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setName("rls_users_policy"))
                        .setForeignKeys(asList(new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration().setTableName("tabXXX"), new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration().setTableName("comments"))),
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
