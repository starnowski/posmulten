package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

class IsRecordBelongsToCurrentTenantConstraintProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantConstraintProducer()

    @Unroll
    def "should return statement (#expectedStatement) that add '#constraintName' constraint to table (#table) and schema (#schema)"()
    {
        given:
        IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                {
                    "cccsss"
                }
        DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(constraintName)
                .withTableName(table)
                .withTableSchema(schema)
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                .withPrimaryColumnsValuesMap()
        expect:
            tested.produce(table, schema) == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" ADD CONSTRAINT;"
//            null        | "posts"   ||  "ALTER TABLE \"posts\" FORCE ROW LEVEL SECURITY;"
//            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" FORCE ROW LEVEL SECURITY;"
//            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" FORCE ROW LEVEL SECURITY;"
//            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" FORCE ROW LEVEL SECURITY;"
    }
}
