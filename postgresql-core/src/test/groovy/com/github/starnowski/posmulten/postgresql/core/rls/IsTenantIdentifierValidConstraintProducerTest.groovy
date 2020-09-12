package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum.REFERENCE

class IsTenantIdentifierValidConstraintProducerTest extends AbstractConstraintProducerTest<IIsTenantIdentifierValidConstraintProducerParameters, IsTenantIdentifierValidConstraintProducer> {

    def tested = new IsTenantIdentifierValidConstraintProducer()

    @Unroll
    def "should return statement (#expectedStatement) that adds '#constraintName' constraint to table (#table) and schema (#schema) and tenant column #tenantColumn"()
    {
        given:
            FunctionArgumentValue capturedFunctionArgument = null
            IIsTenantValidFunctionInvocationFactory factory =
                    {arguments ->
                        capturedFunctionArgument = arguments
                        conditionStatement
                    }
            def parameters = DefaultIsTenantIdentifierValidConstraintProducerParameters.builder()
                    .withConstraintName(constraintName)
                    .withTableName(table)
                    .withTableSchema(schema)
                    .withIIsTenantValidFunctionInvocationFactory(factory)
                    .withTenantColumnName(tenantColumn)build()
        when:
            def definition = tested.produce(parameters)

        then:
            definition.getCreateScript() == expectedStatement

        and: "correct tenant column parameter should be passed to the component that implements the IIsTenantValidFunctionInvocationFactory type"
            capturedFunctionArgument.getValue() == tenantColumn
            capturedFunctionArgument.getType() == REFERENCE

        where:
            constraintName      |   schema      | table     |   conditionStatement              |   tenantColumn            ||	expectedStatement
            "sss"               |   null        | "users"   |   "cccsss"                        |   "d_tenant"              ||  "ALTER TABLE \"users\" ADD CONSTRAINT sss CHECK ((id IS NULL) OR (cccsss));"
            "sss"               |   "public"    | "users"   |   "cccsss"                        |   "tenant"                ||  "ALTER TABLE \"public\".\"users\" ADD CONSTRAINT sss CHECK ((abc_user_id IS NULL AND id IS NULL) OR (cccsss));"
            "sss"               |   "secondary" | "users"   |   "cccsss"                        |   "tenant_id"             ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT sss CHECK ((abc_user_id IS NULL AND userId IS NULL) OR (cccsss));"
            "user_belongs_tt"   |   "secondary" | "users"   |   "cccsss"                        |   "t_id"                  ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK ((uuid IS NULL) OR (cccsss));"
            "user_belongs_tt"   |   "secondary" | "users"   |   "is_tenant_correct(tenant_id)"  |   "tenantId"              ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK ((secondary_colId IS NULL AND uuid IS NULL) OR (is_tenant_correct(tenant_id)));"
            "user_belongs_tt"   |   "secondary" | "users"   |   "is_it_really_my_tenant(t)"     |   "tenant_identifier"     ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK ((a IS NULL AND b IS NULL AND c IS NULL) OR (is_it_really_my_tenant(t)));"
    }

    @Override
    protected IsTenantIdentifierValidConstraintProducer returnTestedObject() {
        tested
    }

    @Override
    protected IIsTenantIdentifierValidConstraintProducerParameters returnCorrectParametersMockObject() {
        IIsTenantValidFunctionInvocationFactory factory =
                { paramerts ->
                    "XXXXXX"
                }
        IIsTenantIdentifierValidConstraintProducerParameters mock = Mock(IIsTenantIdentifierValidConstraintProducerParameters)
        mock.getConstraintName() >> "const_1"
        mock.getTableName() >> "users"
        mock.getTableSchema() >> "public"
        mock.getIIsTenantValidFunctionInvocationFactory() >> factory
        mock.getTenantColumnName() >> "tenant"
        mock
    }
}
