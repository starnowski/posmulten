package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.IntegerRandomizer
import com.github.starnowski.posmulten.configuration.common.yaml.OptionalRandomizer
import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.jeasy.random.randomizers.text.StringDelegatingRandomizer
import spock.lang.Specification

import static java.util.stream.Collectors.toList

abstract class AbstractConfigurationMapperTest<I, O, T extends IConfigurationMapper<I, O>, CMTC extends AbstractConfigurationMapperTestContext> extends Specification {

    protected abstract CMTC getConfigurationMapperTestContext()

    def "should unmap yaml objects to expected configuration objects"() {
        given:
            T tested = getTestedObject()
            List<O> yamlObjects = prepareExpectedMappedObjectsList()
            List<I> expectedObjects = prepareExpectedUnmappeddObjectsList()

        when:
            def actualObjects = yamlObjects.stream().map({ yamlObject -> tested.unmap(yamlObject) }).collect(toList())

        then:
            actualObjects == expectedObjects
    }

    def "should map yaml objects to expected configuration objects"() {
        given:
            T tested = getTestedObject()
            List<I> expectedYamlObjects = prepareExpectedMappedObjectsList()
            List<O> configurationObjects = prepareExpectedUnmappeddObjectsList()

        when:
            def actualObjects = configurationObjects.stream().map({ configurationObject -> tested.map(configurationObject) }).collect(toList())

        then:
            actualObjects == expectedYamlObjects
    }

    def "should unmap random generated yaml configuration object"()
    {
        given:
            T tested = getTestedObject()
            EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(FieldPredicates.named("schema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(getTableEntryClass())), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
                .randomize(FieldPredicates.named("tableSchema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(getForeignKeyConfigurationClass())), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
            EasyRandom easyRandom = new EasyRandom(parameters)
            O yamlConfiguration = easyRandom.nextObject(getYamlConfigurationObjectClass())

        when:
            def configuration = tested.unmap(yamlConfiguration)

        then:
            configuration

        and: "unmapped object should be able to map to an equal object"
            yamlConfiguration == tested.map(configuration)
    }

    def "should map random generated configuration object"()
    {
        given:
            T tested = getTestedObject()
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(FieldPredicates.named("schema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(TableEntry.class)), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
                .randomize(FieldPredicates.named("tableSchema").and(FieldPredicates.ofType(Optional.class)).and(FieldPredicates.inClass(ForeignKeyConfiguration.class)), new OptionalRandomizer(StringDelegatingRandomizer.aNewStringDelegatingRandomizer(new IntegerRandomizer(1, 255)), true))
        EasyRandom easyRandom = new EasyRandom(parameters)
            I configuration = easyRandom.nextObject(getConfigurationObjectClass())

        when:
            def yamlConfiguration = tested.map(configuration)

        then:
            yamlConfiguration

        and: "mapped object should be able to unmap to an equal object"
            configuration == tested.unmap(yamlConfiguration)
    }

    def "should map null object where parameter is null"()
    {
        given:
            T tested = getTestedObject()

        expect:
            null == tested.map(null)

    }

    def "should unmap null object where parameter is null"()
    {
        given:
            T tested = getTestedObject()

        expect:
            null == tested.unmap(null)

    }

    abstract protected Class<O> getConfigurationObjectClass()

    abstract protected Class<I> getYamlConfigurationObjectClass()

    abstract protected T getTestedObject()

    abstract protected List<I> prepareExpectedMappedObjectsList()

    abstract protected List<O> prepareExpectedUnmappeddObjectsList()

    protected <F extends AbstractForeignKeyConfiguration> Class<F> getForeignKeyConfigurationClass() {
        getConfigurationMapperTestContext().getForeignKeyConfigurationClass()
    }
    protected <F extends AbstractTableEntry> Class<F> getTableEntryClass() {
        getConfigurationMapperTestContext().getTableEntryClass()
    }
}
