package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

class IsRecordBelongsToCurrentTenantConstraintProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantConstraintProducer()

    @Unroll
    def "should return statement (#expectedStatement) that adds '#constraintName' constraint to table (#table) and schema (#schema)"()
    {
        given:
        IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                {
                    "cccsss"
                }
        def parameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(constraintName)
                .withTableName(table)
                .withTableSchema(schema)
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                .withPrimaryColumnsValuesMap([:]).build()
        expect:
            tested.produce(parameters) == expectedStatement

        where:
            constraintName  |   schema      | table     ||	expectedStatement
            "sss"           |   null        | "users"   ||  "ALTER TABLE \"users\" ADD CONSTRAINT sss CHECK (cccsss);"
//            null        | "posts"   ||  "ALTER TABLE \"posts\" FORCE ROW LEVEL SECURITY;"
//            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" FORCE ROW LEVEL SECURITY;"
//            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" FORCE ROW LEVEL SECURITY;"
//            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" FORCE ROW LEVEL SECURITY;"
    }
}
