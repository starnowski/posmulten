package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.test.utils.RandomString
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
            Map<String, FunctionArgumentValue> capturedPrimaryColumnsValuesMap = null
            IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                    {arguments ->
                        capturedPrimaryColumnsValuesMap = arguments
                        conditionStatement
                    }
            Map<String, FunctionArgumentValue> primaryColumnsValuesMap = generateRandomPrimaryColumnsValuesMap()
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
            constraintName      |   schema      | table     |   conditionStatement              ||	expectedStatement
            "sss"               |   null        | "users"   |   "cccsss"                        ||  "ALTER TABLE \"users\" ADD CONSTRAINT sss CHECK (cccsss);"
            "sss"               |   "public"    | "users"   |   "cccsss"                        ||  "ALTER TABLE \"public\".\"users\" ADD CONSTRAINT sss CHECK (cccsss);"
            "sss"               |   "secondary" | "users"   |   "cccsss"                        ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT sss CHECK (cccsss);"
            "user_belongs_tt"   |   "secondary" | "users"   |   "cccsss"                        ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK (cccsss);"
            "user_belongs_tt"   |   "secondary" | "users"   |   "is_tenant_correct(tenant_id)"  ||  "ALTER TABLE \"secondary\".\"users\" ADD CONSTRAINT user_belongs_tt CHECK (is_tenant_correct(tenant_id));"
    }

    @Unroll
    def "should return statement (#expectedStatement) that drops '#constraintName' constraint for table (#table) and schema (#schema)"()
    {
        given:
            def randomString = new RandomString(5, new Random(), RandomString.lower)
            IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                    {
                        randomString.nextString()
                    }
            Map<String, FunctionArgumentValue> primaryColumnsValuesMap = generateRandomPrimaryColumnsValuesMap()
            def parameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                    .withConstraintName(constraintName)
                    .withTableName(table)
                    .withTableSchema(schema)
                    .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                    .withPrimaryColumnsValuesMap(primaryColumnsValuesMap).build()
        when:
            def definition = tested.produce(parameters)

        then:
            definition.getDropScript() == expectedStatement

        where:
            constraintName      |   schema      | table     ||	expectedStatement
            "sss"               |   null        | "users"   ||  "ALTER TABLE \"users\" DROP CONSTRAINT IF EXISTS sss;"
            "const_1"           |   "public"    | "users"   ||  "ALTER TABLE \"public\".\"users\" DROP CONSTRAINT IF EXISTS const_1;"
            "sss"               |   "secondary" | "users"   ||  "ALTER TABLE \"secondary\".\"users\" DROP CONSTRAINT IF EXISTS sss;"
            "user_belongs_tt"   |   "secondary" | "users"   ||  "ALTER TABLE \"secondary\".\"users\" DROP CONSTRAINT IF EXISTS user_belongs_tt;"
    }

    @Unroll
    def "should return correct definition based on the generic parameters object"()
    {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            def definition = tested.produce(parameters)

        then:
            definition.getCreateScript() == "ALTER TABLE \"public\".\"users\" ADD CONSTRAINT const_1 CHECK (current_tenant());"
            definition.getDropScript() == "ALTER TABLE \"public\".\"users\" DROP CONSTRAINT IF EXISTS const_1;"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the table name is null" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            tested.produce(parameters)

        then:
            _ * parameters.getTableName() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be null"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the table name is empty (#tableName)" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            tested.produce(parameters)

        then:
            _ * parameters.getTableName() >> tableName
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be empty"

        where:
            tableName << ["", " ", "          "]
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the table schema is empty (#tableSchema)" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            tested.produce(parameters)

        then:
            _ * parameters.getTableSchema() >> tableSchema
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table schema cannot be empty"

        where:
            tableSchema << ["", " ", "          "]
    }

    def "should throw an exception of type 'IllegalArgumentException' when the constraint name is null" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            tested.produce(parameters)

        then:
            _ * parameters.getConstraintName() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Constraint name cannot be null"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the constraint nameis empty (#constraintName()" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            tested.produce(parameters)

        then:
            _ * parameters.getConstraintName() >> constraintName
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Constraint name cannot be empty"

        where:
            constraintName << ["", " ", "          "]
    }

    def "should throw an exception of type 'IllegalArgumentException' when the object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory is null" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            tested.produce(parameters)

        then:
            _ * parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory cannot be null"
    }

    Map<String, FunctionArgumentValue> generateRandomPrimaryColumnsValuesMap()
    {
        def randomString = new RandomString(5, new Random(), RandomString.lower)
        def random = new Random()
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>()
        def mapSize = random.nextInt(5) + 1
        for (int i = 0; i < mapSize; i++)
        {
            primaryColumnsValuesMap.put(randomString.nextString(), forReference(randomString.nextString()))
        }
        primaryColumnsValuesMap
    }

    IsRecordBelongsToCurrentTenantConstraintProducerParameters returnCorrectParametersMockObject() {
        IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                {
                    "current_tenant()"
                }
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = generateRandomPrimaryColumnsValuesMap()
        IsRecordBelongsToCurrentTenantConstraintProducerParameters mock = Mock(IsRecordBelongsToCurrentTenantConstraintProducerParameters)
        mock.getConstraintName() >> "const_1"
        mock.getTableName() >> "users"
        mock.getTableSchema() >> "public"
        mock.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() >> isRecordBelongsToCurrentTenantFunctionInvocationFactory
        mock.getPrimaryColumnsValuesMap() >> primaryColumnsValuesMap
        mock
    }
}
