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
            "sss"               |   null        | "users"   |   "cccsss"                        |   "d_tenant"              ||  "ALTER TABLE \"users\" ADD CONSTRAINT sss CHECK (d_tenant IS NULL OR cccsss);"
            "sss"               |   "public"    | "users"   |   "some_fun(asf)"                 |   "tenant"                ||  "ALTER TABLE \"public\".\"users\" ADD CONSTRAINT sss CHECK (tenant IS NULL OR some_fun(asf));"
            "sss"               |   "secondary" | "users"   |   "xxxx = 3"                      |   "tenant_id"             ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT sss CHECK (tenant_id IS NULL OR xxxx = 3);"
            "user_belongs_tt"   |   "secondary" | "users"   |   "cccsss"                        |   "t_id"                  ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK (t_id IS NULL OR cccsss);"
            "user_belongs_tt"   |   "secondary" | "users"   |   "is_tenant_correct(tenant_id)"  |   "tenantId"              ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK (tenantId IS NULL OR is_tenant_correct(tenant_id));"
            "user_belongs_tt"   |   "secondary" | "users"   |   "is_it_really_my_tenant(t)"     |   "tenant_identifier"     ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK (tenant_identifier IS NULL OR is_it_really_my_tenant(t));"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the object of type IIsTenantValidFunctionInvocationFactory is null" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            tested.produce(parameters)

        then:
            _ * parameters.getIIsTenantValidFunctionInvocationFactory() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Object of type IIsTenantValidFunctionInvocationFactory cannot be null"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the tenant column is null" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            returnTestedObject().produce(parameters)

        then:
            _ * parameters.getTenantColumnName() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Tenant column cannot be null"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the tenant column is empty (#tenantColumn)" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            returnTestedObject().produce(parameters)

        then:
            _ * parameters.getTenantColumnName() >> tenantColumn
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Tenant column cannot be empty"

        where:
            tenantColumn << ["", " ", "          "]
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
