package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.util.Pair
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static IIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType
import static java.lang.String.format

class IsRecordBelongsToCurrentTenantFunctionDefinitionTest extends Specification {

    def "should generate correct function invocation"()
    {
        given:
            def definition = prepareCorrectDefinition()

        expect:
            definition.returnIsRecordBelongsToCurrentTenantFunctionInvocation(preparePrimaryColumnsValuesMap()) == "schema222.record_exists_for_tenant(fk_col1, fk_id2)"
    }

    def "should throw an exception of type 'IllegalArgumentException' when passed parameters list is null"()
    {
        given:
            def definition = prepareCorrectDefinition()

        when:
            definition.returnIsRecordBelongsToCurrentTenantFunctionInvocation(null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The primary columns values map cannot be null"
    }

    def "should throw an exception of type 'IllegalArgumentException' when passed parameters list is empty"()
    {
        given:
            def definition = prepareCorrectDefinition()

        when:
            definition.returnIsRecordBelongsToCurrentTenantFunctionInvocation([:])

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The primary columns values map cannot be empty"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when passed parameters list size is invalid, expected #expectedSize but has #currentSize for function parameters #functionParameters and passed arguments #passedArguments"()
    {
        given:
            def definition = prepareCorrectDefinition(functionParameters)
            def expectedExceptionMessage = format("The primary columns values map has invalid size, expected %s elements but has %s elements", expectedSize, currentSize)

        when:
            definition.returnIsRecordBelongsToCurrentTenantFunctionInvocation(passedArguments)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == expectedExceptionMessage

        where:
            functionParameters                      |   passedArguments                                                                                 |   expectedSize    |   currentSize
            prepareKeyColumnsPairsList()            |   [id2: forReference("fk_id2")]                                                                   |   2               |   1
            prepareKeyColumnsPairsList()            |   [col1: forReference("fk_col1")]                                                                 |   2               |   1
            prepareKeyColumnsPairsList()            |   [col1: forReference("fk_col1"), id2: forReference("fk_id2"), uuid: forReference("fk_uuid")]     |   2               |   3
            [pairOfColumnWithType("id", "UUID")]    |   [id: forReference("user_id"), post_id: forReference("post_fk_id")]                              |   1               |   2
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when passed parameters values does not contains keys for all function parameters for function parameters #functionParameters and passed arguments #passedArguments"()
    {
        given:
            def definition = prepareCorrectDefinition(functionParameters)

        when:
            definition.returnIsRecordBelongsToCurrentTenantFunctionInvocation(passedArguments)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == expectedExceptionMessage

        where:
            functionParameters                              |   passedArguments                                                     |   expectedExceptionMessage
            prepareKeyColumnsPairsList()                    |   [gg: forReference("fk_col1"), id2: forReference("fk_id2")]          |   "The primary columns values map does not contains keys for function arguments: col1"
            prepareKeyColumnsPairsList()                    |   [kk: forReference("fk_col1"), aa: forReference("fk_id2")]           |   "The primary columns values map does not contains keys for function arguments: col1, id2"
            prepareKeyColumnsPairsList()                    |   [col1: forReference("fk_col1"), zzz: forReference("fk_id2")]        |   "The primary columns values map does not contains keys for function arguments: id2"
            [pairOfColumnWithType("user_id", "UUID")]       |   [id: forReference("post_fk_id")]                                    |   "The primary columns values map does not contains keys for function arguments: user_id"
    }


    IsRecordBelongsToCurrentTenantFunctionDefinition prepareCorrectDefinition() {
        IFunctionDefinition functionDefinitionMock = Mock(IFunctionDefinition)
        functionDefinitionMock.getFunctionReference() >> "schema222.record_exists_for_tenant"
        functionDefinitionMock.getFunctionArguments() >> [Mock(IFunctionArgument), Mock(IFunctionArgument)]
        new IsRecordBelongsToCurrentTenantFunctionDefinition(functionDefinitionMock, prepareKeyColumnsPairsList())
    }

    List<Pair<String, IFunctionArgument>> prepareKeyColumnsPairsList()
    {
        [pairOfColumnWithType("col1", "bigint"), pairOfColumnWithType("id2", "bigint")]
    }

    Map<String, FunctionArgumentValue> preparePrimaryColumnsValuesMap() {
        [col1: forReference("fk_col1"), id2: forReference("fk_id2")]
    }

    IsRecordBelongsToCurrentTenantFunctionDefinition prepareCorrectDefinition(List<Pair<String, IFunctionArgument>> prepareKeyColumnsPairsList) {
        IFunctionDefinition functionDefinitionMock = Mock(IFunctionDefinition)
        functionDefinitionMock.getFunctionReference() >> "schema222.record_exists_for_tenant"
        functionDefinitionMock.getFunctionArguments() >> [Mock(IFunctionArgument), Mock(IFunctionArgument)]
        new IsRecordBelongsToCurrentTenantFunctionDefinition(functionDefinitionMock, prepareKeyColumnsPairsList)
    }
}
