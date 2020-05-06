package com.github.starnowski.posmulten.postgresql.core.rls

import spock.lang.Specification

abstract class AbstractFunctionFactoryTest extends Specification {

    def "should return non-empty string object for correct parameters object"() {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()

        when:
            String result = tested.produce(parameters)

        then:
            result != null
            !result.trim().isEmpty()
    }

    def "should throw exception of type 'IllegalArgumentException' when function name is null, even if the rest of parameters are correct"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()

        when:
            String result = tested.produce(parameters)

        then:
            1 * parameters.getFunctionName() >> null
            def ex = thrown(IllegalArgumentException.class)
    }

    abstract protected returnTestedObject();

    abstract protected returnCorrectParametersSpyObject();
}
