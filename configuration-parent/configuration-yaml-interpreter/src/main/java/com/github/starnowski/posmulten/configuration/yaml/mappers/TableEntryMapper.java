package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.core.model.StringWrapper;
import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNullValue;
import com.github.starnowski.posmulten.configuration.yaml.model.TableEntry;

import static java.util.stream.Collectors.toList;

public class TableEntryMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.TableEntry, TableEntry> {

    private final RLSPolicyMapper rlsPolicyMapper = new RLSPolicyMapper();
    private final ForeignKeyConfigurationMapper foreignKeyConfigurationMapper = new ForeignKeyConfigurationMapper();

    @Override
    public TableEntry map(com.github.starnowski.posmulten.configuration.core.model.TableEntry input) {
        return input == null ? null : new TableEntry().setName(input.getName())
                .setSchema(input.getSchema() == null ? null : new StringWrapperWithNullValue(input.getSchema().getValue()))
                .setRlsPolicy(rlsPolicyMapper.map(input.getRlsPolicy()))
                .setForeignKeys(input.getForeignKeys() == null ? null : input.getForeignKeys().stream().map(key -> foreignKeyConfigurationMapper.map(key)).collect(toList()));
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.TableEntry unmap(TableEntry output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.TableEntry().setName(output.getName()).setRlsPolicy(rlsPolicyMapper.unmap(output.getRlsPolicy()))
                .setSchema(output.getSchema() == null ? null : new StringWrapper(output.getSchema().getValue()))
                .setForeignKeys(output.getForeignKeys() == null ? null : output.getForeignKeys().stream().map(key -> foreignKeyConfigurationMapper.unmap(key)).collect(toList()));
    }
}
