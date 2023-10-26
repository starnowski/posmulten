package com.github.starnowski.posmulten.postgresql.core.context.comparable

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

class DefaultSharedSchemaContextComparatorTest extends Specification {

    @Unroll
    def "should return correct differences for creation scripts for left #left and right #right"(){
        given:
            SharedSchemaContextComparator tested = new DefaultSharedSchemaContextComparator()
            ISharedSchemaContext leftContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext rightContext = Mock(ISharedSchemaContext)
            leftContext.getSqlDefinitions() >> {
                left.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCreateScript() >> it
                    defMock
                }).collect(Collectors.toList())
            }
            rightContext.getSqlDefinitions() >> {
                right.stream().map({
                    def defMock = Mock(SQLDefinition)
                    defMock.getCreateScript() >> it
                    defMock
                }).collect(Collectors.toList())
            }

        when:
            SharedSchemaContextComparator.SharedSchemaContextComparableResults result = tested.compare(leftContext, rightContext)

        then:
            result.get

        where:
            left | right
            []      | []
    }
}
