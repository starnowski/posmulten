package com.github.starnowski.posmulten.postgresql.core.rls.function

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType

class IsRecordBelongsToCurrentTenantProducerTest extends Specification {

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
            testSchema              |   testFunctionName                        |   recordTableName     |   recordSchemaName    |   getCurrentTenantFunction    |   tenantColumnPair                                |   keyColumnsPairs                         || expectedStatement
            null                    |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   "current_tenant()"          |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]  ||  "CREATE OR REPLACE FUNCTION is_user_belongs_to_current_tenant(bigint, text) RETURNS BOOLEAN AS \$\$\nSELECT EXISTS (\n(SELECT 1 FROM user_info ui WHERE ui.user_id = \$1 AND ui.tenant_id = current_setting('current_tenant'))\n)\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
    }
}
