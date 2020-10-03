package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.enrichers.ISharedSchemaContextEnricher
import spock.lang.Specification

class DefaultSharedSchemaContextBuilderTest extends Specification {

    def "should return null when clone method for the request object throws exception"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            def request = Mock(SharedSchemaContextRequest)
            request.clone() >> { throw  new CloneNotSupportedException("test")}

        when:
            def result = tested.getSharedSchemaContextRequestCopyOrNull(request)

        then:
            result == null
    }

    def "should return empty list of enrichers when list has value null"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
                .setEnrichers(null)

        when:
            def result = tested.getEnrichersCopy()

        then:
            result != null
            result.isEmpty()
    }

    def "should return always new copy of enrichers list"()
    {
        given:
        def enricher = Mock(ISharedSchemaContextEnricher)
        def tested = new DefaultSharedSchemaContextBuilder()
                .setEnrichers([enricher])

        when:
        def result1 = tested.getEnrichersCopy()
        def result2 = tested.getEnrichersCopy()

        then:
            result1 != null
            result2 != null
            !result1.is(result2)

        and: "list has correct size"
            result1.size() == 1
            result2.size() == 1

        and: "list should contains reference to passed enricher"
            result1.contains(enricher)
            result2.contains(enricher)

        and: "returned lists should be equal"
            result1 == result2
    }
}
