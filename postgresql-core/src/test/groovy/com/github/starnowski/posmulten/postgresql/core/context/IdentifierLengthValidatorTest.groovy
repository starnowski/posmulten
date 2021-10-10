package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException
import spock.lang.Specification
import spock.lang.Unroll

class IdentifierLengthValidatorTest extends Specification {

    def tested = new IdentifierLengthValidator()

    @Unroll
    def "should throw exception of type 'SharedSchemaContextBuilderException' when identifierMinLength is less or equal to zero (#identifierMinLength)"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest();
            request.setIdentifierMinLength(identifierMinLength)

        when:
            tested.init(request)

        then:
            def ex = thrown(InvalidSharedSchemaContextRequestException.class)

        and: "exception should have correct message"
            ex.message == "The identifierMinLength property value can not be less or equal to zero"

        where:
            identifierMinLength << [0, -1, -100]
    }
}
