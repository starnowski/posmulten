package com.github.starnowski.posmulten.postgresql.core.context

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
}
