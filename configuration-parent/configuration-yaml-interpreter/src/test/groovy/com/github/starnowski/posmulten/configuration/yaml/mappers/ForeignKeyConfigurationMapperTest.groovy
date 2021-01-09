package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

class ForeignKeyConfigurationMapperTest extends AbstractConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration, com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration, ForeignKeyConfigurationMapper> {

    @Override
    protected Class<ForeignKeyConfiguration> getConfigurationObjectClass() {
        ForeignKeyConfiguration.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration.class
    }

    @Override
    protected ForeignKeyConfigurationMapper getTestedObject() {
        new ForeignKeyConfigurationMapper()
    }

    @Override
    protected List<com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration> prepareExpectedMappedObjectsList() {
        [
                new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration(),
                new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration(),
                new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration().setConstraintName("asfxzvz")
                        .setTableName("ccc"),
                new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration().setConstraintName("fk_constraint")
                        .setTableName("some_table")
                        .setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("user_id", "id").build()),
                new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration().setConstraintName("users_fk")
                        .setTableName("users_po_table")
                        .setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("sss", "uuid").put("some_id", "primary_key_col_id").build())
        ]
    }

    @Override
    protected List<ForeignKeyConfiguration> prepareExpectedUnmappeddObjectsList() {
        [
                new ForeignKeyConfiguration(),
                new ForeignKeyConfiguration().setConstraintName(null)
                        .setTableName(null),
                new ForeignKeyConfiguration().setConstraintName("asfxzvz")
                        .setTableName("ccc"),
                new ForeignKeyConfiguration().setConstraintName("fk_constraint")
                        .setTableName("some_table")
                        .setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("user_id", "id").build()),
                new ForeignKeyConfiguration().setConstraintName("users_fk")
                        .setTableName("users_po_table")
                        .setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("sss", "uuid").put("some_id", "primary_key_col_id").build())
        ]
    }
}
