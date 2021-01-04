package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static java.util.Arrays.asList
import static java.util.stream.Collectors.toList

class TablesEntriesEnricherTest extends Specification {

    TablesEntriesEnricher tested

    def "should invoke enrichers components for all table entries"()
    {
        given:
            def enricher1 = Mock(ITableEntryEnricher)
            def enricher2 = Mock(ITableEntryEnricher)
            def enricher3 = Mock(ITableEntryEnricher)
            tested = new TablesEntriesEnricher(asList(enricher1, enricher2, enricher3))
            def builder = Mock(DefaultSharedSchemaContextBuilder)
            def tableEntry1 = new TableEntry()
            def tableEntry2 = new TableEntry()

        when:
            def result = tested.enrich(builder, asList(tableEntry1, tableEntry2))

        then:
            result
            result.is(builder)
            1 * enricher1.enrich(builder, tableEntry1)
            1 * enricher1.enrich(builder, tableEntry2)
            1 * enricher2.enrich(builder, tableEntry1)
            1 * enricher2.enrich(builder, tableEntry2)
            1 * enricher3.enrich(builder, tableEntry1)
            1 * enricher3.enrich(builder, tableEntry2)
    }

    @Unroll
    def "should not invoke enrichers components when there is no table entries (#tableEntries)"() {
        given:
            def enricher1 = Mock(ITableEntryEnricher)
            def enricher2 = Mock(ITableEntryEnricher)
            tested = new TablesEntriesEnricher(asList(enricher1, enricher2))
            def builder = Mock(DefaultSharedSchemaContextBuilder)

        when:
            def result = tested.enrich(builder, tableEntries)

        then:
            result
            result.is(builder)
            0 * enricher1.enrich(builder, _)
            0 * enricher2.enrich(builder, _)

        where:
            tableEntries << [null, []]
    }

    def "should be initialized with expected component types"()
    {
        given:
            def expectedComponentsTypes = Arrays.asList(RLSPolicyConfigurationEnricher.class, ForeignKeyConfigurationsEnricher.class)

        when:
            def result = new TablesEntriesEnricher()

        then:
            result
            result.getEnrichers().stream().map({component -> component.getClass()}).collect(toList()).containsAll(expectedComponentsTypes)

    }
}
