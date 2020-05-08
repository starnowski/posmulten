package com.github.starnowski.posmulten.postgresql.core

import com.github.starnowski.posmulten.postgresql.core.rls.IFunctionFactoryParameters
import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractFunctionFactoryTest extends Specification {

    def "should return non-empty string object for correct parameters object"() {
        given:
        AbstractFunctionFactory tested = returnTestedObject()
        IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()

        when:
            String result = tested.produce(parameters).getCreateScript()

        then:
            result != null
            !result.trim().isEmpty()
    }

    def "should throw exception of type 'IllegalArgumentException' when parameters object is null" ()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()

        when:
            tested.produce(null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The parameters object cannot be null"
    }

    def "should throw exception of type 'IllegalArgumentException' when function name is null, even if the rest of parameters are correct"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()

        when:
            tested.produce(parameters)

        then:
            1 * parameters.getFunctionName() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Function name cannot be null"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when function name is blank ('#functionName'), even if the rest of parameters are correct"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()

        when:
            tested.produce(parameters)

        then:
            (1.._) * parameters.getFunctionName() >> functionName
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Function name cannot be blank"

        where:
            functionName << ["", "  ", "            "]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when schema name is blank ('#schema'), even if the rest of parameters are correct"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()

        when:
            tested.produce(parameters)

        then:
            (1.._) * parameters.getSchema() >> schema
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Schema name cannot be blank"

        where:
            schema << ["", "  ", "            "]
    }

    @Unroll
    def "should return expected reference to function #expectedFunctionReference for schema #schema and function name #functionName"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> functionName

        expect:
            tested.produce(parameters).getFunctionReference() == expectedFunctionReference

        where:
            schema      |   functionName            ||  expectedFunctionReference
            null        |   "fun1"                  ||  "fun1"
            "public"    |   "fun1"                  ||  "public.fun1"
            "sch"       |   "fun1"                  ||  "sch.fun1"
            null        |   "this_is_function"      ||  "this_is_function"
            "public"    |   "this_is_function"      ||  "public.this_is_function"
            "sch"       |   "this_is_function"      ||  "sch.this_is_function"
    }

    abstract protected returnTestedObject();

    abstract protected returnCorrectParametersSpyObject();
}
