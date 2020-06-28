package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.RandomString
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference

class IsRecordBelongsToCurrentTenantConstraintProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantConstraintProducer()

    @Unroll
    def "should return statement (#expectedStatement) that adds '#constraintName' constraint to table (#table) and schema (#schema)"()
    {
        given:
        def r = new RandomString(5, new Random(), RandomString.lower)
        Map<String, FunctionArgumentValue> capturedPrimaryColumnsValuesMap = null
        IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                {arguments ->
                    capturedPrimaryColumnsValuesMap = arguments
                    conditionStatement
                }
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>()
        primaryColumnsValuesMap.put(r.nextString(), forReference(r.nextString()))
        def parameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(constraintName)
                .withTableName(table)
                .withTableSchema(schema)
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                .withPrimaryColumnsValuesMap(primaryColumnsValuesMap).build()
        when:
            def definition = tested.produce(parameters)

        then:
            definition.getCreateScript() == expectedStatement

        and: "correct map parameters should be passed to the component that implements the IsRecordBelongsToCurrentTenantFunctionInvocationFactory type"
            primaryColumnsValuesMap == capturedPrimaryColumnsValuesMap

        where:
            constraintName  |   schema      | table     |   conditionStatement  ||	expectedStatement
            "sss"           |   null        | "users"   |   "cccsss"            ||  "ALTER TABLE \"users\" ADD CONSTRAINT sss CHECK (cccsss);"
//            null        | "posts"   ||  "ALTER TABLE \"posts\" FORCE ROW LEVEL SECURITY;"
//            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" FORCE ROW LEVEL SECURITY;"
//            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" FORCE ROW LEVEL SECURITY;"
//            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" FORCE ROW LEVEL SECURITY;"
    }
}
