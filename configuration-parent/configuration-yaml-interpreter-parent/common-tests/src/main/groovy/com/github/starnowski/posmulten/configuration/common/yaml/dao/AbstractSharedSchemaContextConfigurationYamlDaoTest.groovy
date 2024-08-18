package com.github.starnowski.posmulten.configuration.common.yaml.dao

import com.github.starnowski.posmulten.configuration.common.yaml.AbstractSpecification
import com.github.starnowski.posmulten.configuration.common.yaml.IntegerRandomizer
import com.github.starnowski.posmulten.configuration.common.yaml.OptionalRandomizer
import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.dao.AbstractSharedSchemaContextConfigurationYamlDao
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractCustomDefinitionEntry
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSqlDefinitionsValidation
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.jeasy.random.randomizers.misc.EnumRandomizer
import org.jeasy.random.randomizers.text.StringDelegatingRandomizer
import org.junit.Rule
import org.junit.rules.TemporaryFolder

abstract class AbstractSharedSchemaContextConfigurationYamlDaoTest<SSCC extends AbstractSharedSchemaContextConfiguration, T extends AbstractSharedSchemaContextConfigurationYamlDao<SSCC>> extends AbstractSpecification {


    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder()

    protected T tested = getSharedSchemaContextConfigurationYamlDao()

    protected abstract T getSharedSchemaContextConfigurationYamlDao()

    protected abstract Class<SSCC> getSharedSchemaContextConfigurationClass()

    protected abstract <P extends AbstractSqlDefinitionsValidation> Class<P> getSqlDefinitionsValidationClass()
    protected abstract <P extends AbstractTableEntry> Class<P> getTableEntryClass()
    protected abstract <P extends AbstractForeignKeyConfiguration> Class<P> getForeignKeyConfigurationClass()
    protected abstract <P extends AbstractCustomDefinitionEntry> Class<P> getCustomDefinitionEntryClass()

    def "should return object based on file which was created based on random generated object"()
    {
        given:
            EasyRandomParameters parameters = new EasyRandomParameters()
                    .randomize(FieldPredicates.named("identifierMaxLength").and(FieldPredicates.ofType(Integer.class)).and(FieldPredicates.inClass(getSqlDefinitionsValidationClass())), new IntegerRandomizer(1, 255))
                    .randomize(FieldPredicates.named("identifierMinLength").and(FieldPredicates.ofType(Integer.class)).and(FieldPredicates.inClass(getSqlDefinitionsValidationClass())), new IntegerRandomizer(1, 255))
                    .randomize(FieldPredicates.named("schema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(getTableEntryClass())), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
                    .randomize(FieldPredicates.named("tableSchema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(getForeignKeyConfigurationClass())), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
                    .randomize(FieldPredicates.named("position").and(FieldPredicates.ofType(String.class)).and(FieldPredicates.inClass(getCustomDefinitionEntryClass())), StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new EnumRandomizer(com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition)))
            EasyRandom easyRandom = new EasyRandom(parameters)
            def randomObject = easyRandom.nextObject(getSharedSchemaContextConfigurationClass())
            def tmpFile = tempFolder.newFile("rand-temp-config.yaml")
            tested.save(randomObject, tmpFile.getAbsoluteFile().getAbsolutePath())

        when:
            def result = tested.read(tmpFile.getAbsoluteFile().getAbsolutePath())

        then:
            result

        and: "tests objects should be equal"
            result == randomObject
    }
}
