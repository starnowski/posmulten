package com.github.starnowski.posmulten.postgresql.core.rls


import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractConstraintProducerTest<X extends IConstraintProducerParameters, P extends AbstractConstraintProducer<X>> extends Specification {

    @Unroll
    def "should return correct definition based on the generic parameters object"()
    {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            def definition = returnTestedObject().produce(parameters)

        then:
            definition.getCreateScript() ==~ /ALTER TABLE "public"\."users" ADD CONSTRAINT const_1 CHECK .*;/
            definition.getDropScript() == "ALTER TABLE \"public\".\"users\" DROP CONSTRAINT IF EXISTS const_1;"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the parameters object is null" () {
        when:
            returnTestedObject().produce(null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The parameters object cannot be null"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the table name is null" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            returnTestedObject().produce(parameters)

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
            returnTestedObject().produce(parameters)

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
            returnTestedObject().produce(parameters)

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
            returnTestedObject().produce(parameters)

        then:
            _ * parameters.getConstraintName() >> null
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Constraint name cannot be null"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the constraint name is empty (#constraintName()" () {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            returnTestedObject().produce(parameters)

        then:
            _ * parameters.getConstraintName() >> constraintName
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Constraint name cannot be empty"

        where:
            constraintName << ["", " ", "          "]
    }

    protected abstract P returnTestedObject()

    protected abstract X returnCorrectParametersMockObject()
}
