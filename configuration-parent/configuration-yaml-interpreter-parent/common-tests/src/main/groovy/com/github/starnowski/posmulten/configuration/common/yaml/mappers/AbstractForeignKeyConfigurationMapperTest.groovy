package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

abstract class AbstractForeignKeyConfigurationMapperTest <T extends AbstractForeignKeyConfiguration<T>, M extends IConfigurationMapper<ForeignKeyConfiguration, T>, CMTC extends AbstractConfigurationMapperTestContext>  extends AbstractConfigurationMapperTest<T,  ForeignKeyConfiguration, M, CMTC> {

    @Override
    protected Class<ForeignKeyConfiguration> getConfigurationObjectClass() {
        ForeignKeyConfiguration.class
    }

    protected abstract T createOutputInstance();

    @Override
    protected List<T> prepareExpectedMappedObjectsList() {
        [
                createOutputInstance(),
                createOutputInstance(),
                createOutputInstance().setConstraintName("asfxzvz")
                        .setTableName("ccc"),
                createOutputInstance().setConstraintName("fk_constraint")
                        .setTableName("some_table")
                        .setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("user_id", "id").build()),
                createOutputInstance().setConstraintName("users_fk")
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
