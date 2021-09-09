package com.github.starnowski.posmulten.postgresql.core.rls


import spock.lang.Specification
import spock.lang.Unroll

abstract class AbstractConstraintProducerTest<X extends IConstraintProducerParameters, P extends AbstractConstraintProducer<X>> extends Specification {

    @Unroll
    def "should return correct definition based on the generic parameters object for table #table and schema #schema with constraint name #constraintName"()
    {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            def definition = returnTestedObject().produce(parameters)

        then:
            _ * parameters.getConstraintName() >> constraintName
            _ * parameters.getTableName() >> table
            _ * parameters.getTableSchema() >> schema
            definition.getDropScript() == expectedDropStatement
            definition.getCreateScript() ==~ expectedCreateStatementPattern

        where:
            table           |   schema          |   constraintName          ||  expectedDropStatement                                                                       |   expectedCreateStatementPattern
            "users"         |   "public"        |    "const_1"              ||  "ALTER TABLE \"public\".\"users\" DROP CONSTRAINT IF EXISTS const_1;"                       |   /ALTER TABLE "public"\."users" ADD CONSTRAINT const_1 CHECK .*;/
            "users"         |   null            |    "const_1"              ||  "ALTER TABLE \"users\" DROP CONSTRAINT IF EXISTS const_1;"                                  |   /ALTER TABLE "users" ADD CONSTRAINT const_1 CHECK .*;/
            "users"         |   null            |    "constraint_222"       ||  "ALTER TABLE \"users\" DROP CONSTRAINT IF EXISTS constraint_222;"                           |   /ALTER TABLE "users" ADD CONSTRAINT constraint_222 CHECK .*;/
            "notifications" |   null            |    "constraint_XXX"       ||  "ALTER TABLE \"notifications\" DROP CONSTRAINT IF EXISTS constraint_XXX;"                   |   /ALTER TABLE "notifications" ADD CONSTRAINT constraint_XXX CHECK .*;/
            "notifications" |   "other_schema"  |    "fk_constraint"        ||  "ALTER TABLE \"other_schema\".\"notifications\" DROP CONSTRAINT IF EXISTS fk_constraint;"   |   /ALTER TABLE "other_schema"\."notifications" ADD CONSTRAINT fk_constraint CHECK .*;/
    }

    @Unroll
    def "should return correct checking statements based on the generic parameters object for table #table and schema #schema with constraint name #constraintName"()
    {
        given:
            def parameters = returnCorrectParametersMockObject()

        when:
            def definition = returnTestedObject().produce(parameters)

        then:
            _ * parameters.getConstraintName() >> constraintName
            _ * parameters.getTableName() >> table
            _ * parameters.getTableSchema() >> schema
            definition.getCheckingStatements()
            definition.getCheckingStatements().size() >= 1
            definition.getCheckingStatements().contains(expectedStatement)

        where:
            table           |   schema          |   constraintName          ||  expectedStatement
            "users"         |   "public"        |    "const_1"              ||  checkingStatement("public", "users", "const_1")
            "users"         |   null            |    "const_1"              ||  checkingStatement(null, "users", "const_1")
            "users"         |   null            |    "constraint_222"       ||  checkingStatement(null, "users", "constraint_222")
            "notifications" |   null            |    "constraint_XXX"       ||  checkingStatement(null, "notifications", "constraint_XXX")
            "notifications" |   "other_schema"  |    "fk_constraint"        ||  checkingStatement("other_schema", "notifications", "fk_constraint")
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

    private static String checkingStatement(String schema, String table, String constraintName)
    {
        def template = "SELECT COUNT(1)\n" +
                "\t\tFROM information_schema.table_constraints\n" +
                "\t\tWHERE table_schema = '%s' AND table_name = '%s' AND constraint_name = '%s';"
        String.format(template, schema == null ? "public" : schema, table, constraintName)
    }
}
