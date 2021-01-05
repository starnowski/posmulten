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
            def tableEntry = new TableEntry().setName(table).setForeignKeys(asList(fk1, fk2))

        when:
            def result = tested.enrich(builder, tableEntry)

        then:
            result == builder
            1 * component.enrich(builder, table, fk1)
            1 * component.enrich(builder, table, fk2)

        and: "do not invoke builder"
            0 * builder._

        where:
            table << ["tab1", "users"]
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
}
