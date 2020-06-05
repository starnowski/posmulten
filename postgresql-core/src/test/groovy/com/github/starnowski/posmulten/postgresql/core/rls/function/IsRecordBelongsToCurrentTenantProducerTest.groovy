package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import javafx.util.Pair
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forNumeric
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType

class IsRecordBelongsToCurrentTenantProducerTest extends AbstractFunctionFactoryTest {

    def tested = new IsRecordBelongsToCurrentTenantProducer()

    @Unroll
    def "for function name '#testFunctionName' for schema '#testSchema', table #recordTableName in schema #recordSchemaName that compares values for columns #keyColumnsPairs and tenant column #tenantColumnPair, should generate statement that creates function : #expectedStatement" () {
        given:
            IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory =
                    {
                        getCurrentTenantFunction
                    }
            def parameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                    .withSchema(testSchema)
                    .withFunctionName(testFunctionName)
                    .withRecordTableName(recordTableName)
                    .withRecordSchemaName(recordSchemaName)
                    .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
                    .withTenantColumn(tenantColumn)
                    .withKeyColumnsPairsList(keyColumnsPairs).build()

        expect:
            tested.produce(parameters).getCreateScript() == expectedStatement

        where:
            testSchema              |   testFunctionName                        |   recordTableName     |   recordSchemaName    |   getCurrentTenantFunction    |   tenantColumn                                |   keyColumnsPairs                                                                         || expectedStatement
            null                    |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   "current_tenant()"          |   "tenant_id"         |   [pairOfColumnWithType("id", "bigint")]                                                  ||  "CREATE OR REPLACE FUNCTION is_user_belongs_to_current_tenant(bigint) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM users rt WHERE rt.id = \$1 AND rt.tenant_id = current_tenant()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "is_user_belongs_to_current_tenant"     |   "groups"            |   null                |   "current_tenant()"          |   "tenant_id"         |   [pairOfColumnWithType("id", "bigint")]                                                  ||  "CREATE OR REPLACE FUNCTION public.is_user_belongs_to_current_tenant(bigint) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM groups rt WHERE rt.id = \$1 AND rt.tenant_id = current_tenant()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "is_current_tenant_groups"              |   "groups"            |   "secondary"         |   "cur_ten()"                 |   "tenant_id"         |   [pairOfColumnWithType("id", "bigint")]                                                  ||  "CREATE OR REPLACE FUNCTION public.is_current_tenant_groups(bigint) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM secondary.groups rt WHERE rt.id = \$1 AND rt.tenant_id = cur_ten()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "secondary"             |   "is_posts_belong_to_tenant"             |   "posts"             |   "third"             |   "cur_ten()"                 |   "tenant"            |   [pairOfColumnWithType("uuid", "UUID")]                                                  ||  "CREATE OR REPLACE FUNCTION secondary.is_posts_belong_to_tenant(UUID) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM third.posts rt WHERE rt.uuid = \$1 AND rt.tenant = cur_ten()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "some_schema"           |   "is_user_belongs_to_current_tenant"     |   "user_info"         |   "pub"               |   "cur_ten()"                 |   "tenant_id"         |   [pairOfColumnWithType("uuid", "UUID"), pairOfColumnWithType("second_id", "bigint")]     ||  "CREATE OR REPLACE FUNCTION some_schema.is_user_belongs_to_current_tenant(UUID, bigint) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM pub.user_info rt WHERE rt.uuid = \$1 AND rt.second_id = \$2 AND rt.tenant_id = cur_ten()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
    }

    @Unroll
    def "for function name '#testFunctionName' for schema '#testSchema' that compares values for columns #keyColumnsPairs and their values #keyColumnsValues, tenant column value #tenantColumnValue, should generate statement that invokes function : #expectedStatement" () {
        given:
            IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory =
                    {
                        "current_tenant()"
                    }
            def parameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                    .withSchema(testSchema)
                    .withFunctionName(testFunctionName)
                    .withRecordTableName("some_record_table")
                    .withRecordSchemaName("some_record_schema")
                    .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
                    .withTenantColumn("tenant_id")
                    .withKeyColumnsPairsList(keyColumnsPairs).build()

        expect:
            tested.produce(parameters).returnIsRecordBelongsToCurrentTenantFunctionInvocation(keyColumnsValues) == expectedStatement

        where:
            testSchema              |   testFunctionName                        |   keyColumnsPairs                                                                 |   keyColumnsValues                                    || expectedStatement
            null                    |   "is_user_belongs_to_current_tenant"     |   [pairOfColumnWithType("id", "bigint")]                                          |   [id: forReference("id")]                            ||  "is_user_belongs_to_current_tenant(id)"
            "public"                |   "is_user_belongs_to_current_tenant"     |   [pairOfColumnWithType("id", "bigint")]                                          |   [id: forReference("id")]                            ||  "public.is_user_belongs_to_current_tenant(id)"
            "some_schema"           |   "is_user_belongs_to_current_tenant"     |   [pairOfColumnWithType("id", "bigint")]                                          |   [id: forReference("id")]                            ||  "some_schema.is_user_belongs_to_current_tenant(id)"
            "schema222"             |   "record_exists_for_tenant"              |   [pairOfColumnWithType("id", "bigint")]                                          |   [id: forReference("id")]                            ||  "schema222.record_exists_for_tenant(id)"
            "schema222"             |   "record_exists_for_tenant"              |   [pairOfColumnWithType("id", "bigint")]                                          |   [id: forReference("table_id")]                      ||  "schema222.record_exists_for_tenant(table_id)"
            "schema222"             |   "record_exists_for_tenant"              |   [pairOfColumnWithType("id", "bigint"), pairOfColumnWithType("id2", "bigint")]   |   [id: forReference("col1"), id2: forNumeric("323")]  ||  "schema222.record_exists_for_tenant(col1, 323)"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the record schema name is blank '#recordSchemaName'" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getRecordSchemaName() >> recordSchemaName

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Record schema name cannot be blank"

        where:
            recordSchemaName    << ["", " ", "       "]
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the record table name is blank '#recordTableName'" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getRecordTableName() >> recordTableName

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Record table name cannot be blank"

        where:
            recordTableName    << ["", " ", "       "]
    }

    def "should throw an exception of type 'IllegalArgumentException' when the record table name is null" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getRecordTableName() >> null

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Record table name cannot be null"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the tenant column is null" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getTenantColumn() >> null

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Tenant column cannot be null"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the tenant column is blank '#tenantColumn'" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getTenantColumn() >> tenantColumn

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Tenant column cannot be blank"

        where:
            tenantColumn    << ["", " ", "       "]
    }

    def "should throw an exception of type 'IllegalArgumentException' when the component of type IGetCurrentTenantIdFunctionInvocationFactory is null" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getIGetCurrentTenantIdFunctionInvocationFactory() >> null

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The GetCurrentTenantId function invocation factory cannot be null"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the list of column pairs is null" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getKeyColumnsPairsList() >> null

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The list of primary key column pairs cannot be null"
    }

    def "should throw an exception of type 'IllegalArgumentException' when the list of column pairs is empty" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getKeyColumnsPairsList() >> []

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The list of primary key column pairs cannot be empty"
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the list element of primary key column pairs is null '#keyColumnsPairsList'" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getKeyColumnsPairsList() >> keyColumnsPairsList

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The list element of primary key column pairs cannot be null"

        where:
            keyColumnsPairsList    << [[null], [null, pairOfColumnWithType("id", "bigint")], [pairOfColumnWithType("id", "bigint"), null], [pairOfColumnWithType("id", "bigint"), null, pairOfColumnWithType("id2", "bigint")]]
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the list of primary key column pairs contains pair which key is null '#keyColumnsPairsList'" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getKeyColumnsPairsList() >> keyColumnsPairsList

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The list of primary key column pairs contains pair which key is null"

        where:
            keyColumnsPairsList    << [[pairOfColumnWithType(null, "bigint")], [pairOfColumnWithType(null, "bigint"), pairOfColumnWithType("id2", "bigint")]]
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' when the list of primary key column pairs contains pair which key is blank :'#key'" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getKeyColumnsPairsList() >> [pairOfColumnWithType(key, "bigint")]

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The list of primary key column pairs contains pair which key is blank"

        where:
            key << ["", " ", "      "]
    }

    @Unroll
    def "should throw an exception of type 'IllegalArgumentException' with expected message '#expectedMessage' when the list of primary key column pairs contains pair which value is null for column '#key'" () {
        given:
            def parameters = returnCorrectParametersSpyObject()
            parameters.getKeyColumnsPairsList() >> [new Pair<>(key, null)]

        when:
            tested.produce(parameters)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == expectedMessage

        where:
            key         ||  expectedMessage
            "id"        ||  "The list of primary key column pairs contains pair which value is null for key 'id'"
            "uuid"      ||  "The list of primary key column pairs contains pair which value is null for key 'uuid'"
            "user_id"   ||  "The list of primary key column pairs contains pair which value is null for key 'user_id'"
    }

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory =
                {
                    "current_tenant()"
                }
        Spy(IsRecordBelongsToCurrentTenantProducerParameters, constructorArgs: ["is_user_belongs_to_current_tenant",
                                                                                "public",
                                                                                [pairOfColumnWithType("uuid", "UUID"), pairOfColumnWithType("second_id", "bigint")],
                                                                                "tenant_id",
                                                                                "users",
                                                                                "secondary",
                                                                                getCurrentTenantIdFunctionInvocationFactory])
    }
}
