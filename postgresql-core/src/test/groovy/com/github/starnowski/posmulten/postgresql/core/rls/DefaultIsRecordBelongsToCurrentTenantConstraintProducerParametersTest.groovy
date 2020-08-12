package com.github.starnowski.posmulten.postgresql.core.rls


import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import spock.lang.Specification

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference

class DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersTest extends Specification {

    def "should copy collection of key pairs into different map"()
    {
        given:
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>()
        primaryColumnsValuesMap.put("id", forReference("parent_comment_id"))

        when:
            def parameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                    .withPrimaryColumnsValuesMap(primaryColumnsValuesMap).build()

        then:
            parameters.getPrimaryColumnsValuesMap() == primaryColumnsValuesMap

        and: "map object should not have same reference"
            !parameters.getPrimaryColumnsValuesMap().is(primaryColumnsValuesMap)
    }
}
