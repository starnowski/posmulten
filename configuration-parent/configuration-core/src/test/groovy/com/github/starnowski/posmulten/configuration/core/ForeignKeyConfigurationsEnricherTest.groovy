package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import spock.lang.Unroll

import static java.util.Arrays.asList

class ForeignKeyConfigurationsEnricherTest extends AbstractBaseTest {

    @Unroll
    def "should invoke component that handles foreign key configuration for each foreign key entry in table #table"()
    {
        given:
            def component = Mock(ForeignKeyConfigurationEnricher)
            def tested = new ForeignKeyConfigurationsEnricher(component)
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def fk1 = new ForeignKeyConfiguration().setConstraintName("fk1")
            def fk2 = new ForeignKeyConfiguration().setConstraintName("fk2")
            def tableEntry = new TableEntry().setName(table).setSchema(schema).setForeignKeys(asList(fk1, fk2))

        when:
            def result = tested.enrich(builder, tableEntry)

        then:
            result == builder
            1 * component.enrich(builder, table, schema, fk1)
            1 * component.enrich(builder, table, schema, fk2)

        and: "do not invoke builder"
            0 * builder._

        where:
            table       |   schema
            "tab1"      |   null
            "users"     |   null
            "tab1"      |   Optional.ofNullable("XXX")
            "users"     |   Optional.ofNullable(null)
    }

    @Unroll
    def "should not invoke any builder's component method when invalid object is passed (#message)"()
    {
        given:
            def component = Mock(ForeignKeyConfigurationEnricher)
            def tested = new ForeignKeyConfigurationsEnricher(component)
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            0 * component._

        and: "do not invoke builder"
            0 * builder._

        where:
            entry                                                                       |   message
            null                                                                        |   "null object"
            new TableEntry()                                                            |   "table entry with null collection of foreign keys"
            new TableEntry().setForeignKeys(new ArrayList<ForeignKeyConfiguration>())   |   "table entry with empty collection of foreign keys"
    }

    def "should use correct component type"()
    {
        given:
            def tested = new ForeignKeyConfigurationsEnricher()

        when:
            def result = tested.getForeignKeyConfigurationEnricher()

        then:
            result
            result.getClass().equals(ForeignKeyConfigurationEnricher.class)
    }
}
