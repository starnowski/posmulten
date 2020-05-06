package com.github.starnowski.posmulten.postgresql.core.rls

import spock.lang.Specification

abstract class AbstractFunctionFactoryTest extends Specification {

    def "should return non-empty string object for correct parameters object"() {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersObject()

        when:
            String result = tested.produce(parameters)

        then:
            result != null
            !result.trim().isEmpty()
    }

    abstract protected returnTestedObject();

    abstract protected returnCorrectParametersObject();
}
