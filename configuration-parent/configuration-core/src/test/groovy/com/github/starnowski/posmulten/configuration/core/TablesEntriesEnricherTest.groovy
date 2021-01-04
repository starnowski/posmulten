package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import spock.lang.Specification

import static java.util.Arrays.asList

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
}
