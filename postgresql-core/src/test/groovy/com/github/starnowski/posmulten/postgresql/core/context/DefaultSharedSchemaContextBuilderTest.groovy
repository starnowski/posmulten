package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.enrichers.ISharedSchemaContextEnricher
import com.github.starnowski.posmulten.postgresql.core.context.validators.ISharedSchemaContextRequestValidator
import spock.lang.Specification

class DefaultSharedSchemaContextBuilderTest extends Specification {

    def "should return null when clone method for the request object throws exception"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            def request = Mock(SharedSchemaContextRequest)
            def exception = Mock(CloneNotSupportedException)
            request.clone() >> { throw  exception}

        when:
            def result = tested.getSharedSchemaContextRequestCopyOrNull(request)

        then:
            result == null

        and: "print stack trace"
            1 * exception.printStackTrace()
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

    def "should return empty list of validators when list has value null"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
                    .setValidators(null)

        when:
            def result = tested.getValidatorsCopy()

        then:
            result != null
            result.isEmpty()
    }

    def "should return always new copy of validators list"()
    {
        given:
            def validator = Mock(ISharedSchemaContextRequestValidator)
            def tested = new DefaultSharedSchemaContextBuilder()
                    .setValidators([validator])

        when:
            def result1 = tested.getValidatorsCopy()
            def result2 = tested.getValidatorsCopy()

        then:
            result1 != null
            result2 != null
            !result1.is(result2)

        and: "list has correct size"
            result1.size() == 1
            result2.size() == 1

        and: "list should contains reference to passed validator"
            result1.contains(validator)
            result2.contains(validator)

        and: "returned lists should be equal"
            result1 == result2
    }

    def "should return always new copy of request object"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()

        when:
            def result1 = tested.getSharedSchemaContextRequestCopy()
            def result2 = tested.getSharedSchemaContextRequestCopy()

        then:
            result1 != null
            result2 != null
            !result1.is(result2)
    }
}
