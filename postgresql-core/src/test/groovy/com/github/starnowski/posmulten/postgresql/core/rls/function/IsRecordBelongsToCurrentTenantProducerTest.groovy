package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import spock.lang.Unroll

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
                    .withTenantColumnPair(tenantColumnPair)
                    .withKeyColumnsPairsList(keyColumnsPairs).build()

        expect:
            tested.produce(parameters).getCreateScript() == expectedStatement

        where:
            testSchema              |   testFunctionName                        |   recordTableName     |   recordSchemaName    |   getCurrentTenantFunction    |   tenantColumnPair                                |   keyColumnsPairs                                                                         || expectedStatement
            null                    |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   "current_tenant()"          |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]                                                  ||  "CREATE OR REPLACE FUNCTION is_user_belongs_to_current_tenant(bigint, text) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM users rt WHERE rt.id = \$1 AND \$2 = current_tenant()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "is_user_belongs_to_current_tenant"     |   "groups"            |   null                |   "current_tenant()"          |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]                                                  ||  "CREATE OR REPLACE FUNCTION public.is_user_belongs_to_current_tenant(bigint, text) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM groups rt WHERE rt.id = \$1 AND \$2 = current_tenant()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "is_current_tenant_groups"              |   "groups"            |   "secondary"         |   "cur_ten()"                 |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]                                                  ||  "CREATE OR REPLACE FUNCTION public.is_current_tenant_groups(bigint, text) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM secondary.groups rt WHERE rt.id = \$1 AND \$2 = cur_ten()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "secondary"             |   "is_posts_belong_to_tenant"             |   "posts"             |   "third"             |   "cur_ten()"                 |   pairOfColumnWithType("tenant", "VARCHAR")       |   [pairOfColumnWithType("uuid", "UUID")]                                                  ||  "CREATE OR REPLACE FUNCTION secondary.is_posts_belong_to_tenant(UUID, VARCHAR) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM third.posts rt WHERE rt.uuid = \$1 AND \$2 = cur_ten()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "some_schema"           |   "is_user_belongs_to_current_tenant"     |   "user_info"         |   "pub"               |   "cur_ten()"                 |   pairOfColumnWithType("tenant_id", "VARCHAR")    |   [pairOfColumnWithType("uuid", "UUID"), pairOfColumnWithType("second_id", "bigint")]     ||  "CREATE OR REPLACE FUNCTION some_schema.is_user_belongs_to_current_tenant(UUID, bigint, VARCHAR) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n\tSELECT 1 FROM pub.user_info rt WHERE rt.uuid = \$1 AND rt.second_id = \$2 AND \$3 = cur_ten()\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
    }

    @Unroll
    def "for function name '#testFunctionName' for schema '#testSchema' that compares values for columns #keyColumnsPairs and their values #keyColumnsValues, tenant column #tenantColumnPair and it value #tenantColumnValue, should generate statement that invokes function : #expectedStatement" () {
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
                    .withTenantColumnPair(tenantColumnPair)
                    .withKeyColumnsPairsList(keyColumnsPairs).build()

        expect:
            tested.produce(parameters).returnIsRecordBelongsToCurrentTenantFunctionInvocation(tenantColumnValue, keyColumnsValues) == expectedStatement

        where:
            testSchema              |   testFunctionName                        |   tenantColumnPair                                |   keyColumnsPairs                         |   tenantColumnValue           |   keyColumnsValues                || expectedStatement
            null                    |   "is_user_belongs_to_current_tenant"     |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]  |   forReference("tenant_id")   |   [id: forReference("id")]        ||  "is_user_belongs_to_current_tenant(id, tenant_id)"
            "public"                |   "is_user_belongs_to_current_tenant"     |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]  |   forReference("tenant_id")   |   [id: forReference("id")]        ||  "public.is_user_belongs_to_current_tenant(id, tenant_id)"
            "some_schema"           |   "is_user_belongs_to_current_tenant"     |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]  |   forReference("tenant_id")   |   [id: forReference("id")]        ||  "some_schema.is_user_belongs_to_current_tenant(id, tenant_id)"
            "schema222"             |   "record_exists_for_tenant"              |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]  |   forReference("tenant_id")   |   [id: forReference("id")]        ||  "schema222.record_exists_for_tenant(id, tenant_id)"
            "schema222"             |   "record_exists_for_tenant"              |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]  |   forReference("t_column")    |   [id: forReference("table_id")]  ||  "schema222.record_exists_for_tenant(table_id, t_column)"
    }

    @Override
    protected returnTestedObject() {
        return tested
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
                                                                                pairOfColumnWithType("tenant_id", "text"),
                                                                                "users",
                                                                                "secondary",
                                                                                getCurrentTenantIdFunctionInvocationFactory])
    }
}
