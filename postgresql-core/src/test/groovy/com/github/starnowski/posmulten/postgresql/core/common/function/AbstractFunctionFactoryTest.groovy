package com.github.starnowski.posmulten.postgresql.core.common.function

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static java.lang.String.format
import static java.util.Optional.ofNullable

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

    @Unroll
    def "should create correctly the creation statement with contains the right phrase for schema #schema and function #functionName"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> functionName
            def functionDefinition = tested.produce(parameters)
            String functionReference = functionDefinition.getFunctionReference()
            String expectedCreateFunctionPhrase = format("CREATE OR REPLACE FUNCTION %s(%s)", functionReference, prepareArgumentsPhrase(functionDefinition.getFunctionArguments()))

        expect:
            functionDefinition.getCreateScript().contains(expectedCreateFunctionPhrase)

        where:
            schema      |   functionName
            null        |   "fun1"
            "public"    |   "fun1"
            "sch"       |   "fun1"
            null        |   "this_is_function"
            "public"    |   "this_is_function"
            "sch"       |   "this_is_function"
    }

    @Unroll
    def "should create correct drop statement for schema #schema and function #functionName"()
    {
        given:
            AbstractFunctionFactory tested = returnTestedObject()
            IFunctionFactoryParameters parameters = returnCorrectParametersSpyObject()
            parameters.getSchema() >> schema
            parameters.getFunctionName() >> functionName
            def functionDefinition = tested.produce(parameters)
            String functionReference = functionDefinition.getFunctionReference()
            String expectedDropFunctionStatement = format("DROP FUNCTION IF EXISTS %s(%s);", functionReference, prepareArgumentsPhrase(functionDefinition.getFunctionArguments()))

        expect:
            functionDefinition.getDropScript() == expectedDropFunctionStatement

        where:
            schema      |   functionName
            null        |   "fun1"
            "public"    |   "fun1"
            "sch"       |   "fun1"
            null        |   "this_is_function"
            "public"    |   "this_is_function"
            "sch"       |   "this_is_function"
    }

    private String prepareArgumentsPhrase(List<IFunctionArgument> functionArguments)
    {
        ofNullable(functionArguments).orElse(new ArrayList<IFunctionArgument>()).stream().map({ argument -> argument.getType() }).collect(Collectors.joining( ", " ))
    }

    abstract protected returnTestedObject();

    abstract protected returnCorrectParametersSpyObject();
}
