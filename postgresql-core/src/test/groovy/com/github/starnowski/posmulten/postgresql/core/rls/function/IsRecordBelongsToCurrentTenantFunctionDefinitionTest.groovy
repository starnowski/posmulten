package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition
import spock.lang.Specification

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType

class IsRecordBelongsToCurrentTenantFunctionDefinitionTest extends Specification {

    def "should generate correct function invocation"()
    {
        given:
            def definition = prepareCorrectDefinition()

        expect:
            definition.returnIsRecordBelongsToCurrentTenantFunctionInvocation(preparePrimaryColumnsValuesMap()) == "schema222.record_exists_for_tenant(fk_col1, fk_id2)"
    }

    IsRecordBelongsToCurrentTenantFunctionDefinition prepareCorrectDefinition() {
        IFunctionDefinition functionDefinitionMock = Mock(IFunctionDefinition)
        functionDefinitionMock.getFunctionReference() >> "schema222.record_exists_for_tenant"
        functionDefinitionMock.getFunctionArguments() >> [Mock(IFunctionArgument), Mock(IFunctionArgument)]
        new IsRecordBelongsToCurrentTenantFunctionDefinition(functionDefinitionMock, [pairOfColumnWithType("col1", "bigint"), pairOfColumnWithType("id2", "bigint")])
    }

    Map<String, FunctionArgumentValue> preparePrimaryColumnsValuesMap() {
        [col1: forReference("fk_col1"), id2: forReference("fk_id2")]
    }
}
