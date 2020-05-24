package com.github.starnowski.posmulten.postgresql.core.common.function

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString

class FunctionArgumentValueTest extends Specification {

    @Unroll
    def "should create expected string representation #expectedStringResult"()
    {
        expect:
            argument.toString() == expectedStringResult

        where:
            argument                ||  expectedStringResult
            forString("XXDD")       ||  "DefaultFunctionArgumentValue{value='XXDD', type=STRING}"
            forReference("column")  ||  "DefaultFunctionArgumentValue{value='column', type=REFERENCE}"
    }
}
