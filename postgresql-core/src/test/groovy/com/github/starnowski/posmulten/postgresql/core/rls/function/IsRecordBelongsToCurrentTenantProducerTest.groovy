package com.github.starnowski.posmulten.postgresql.core.rls.function

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType

class IsRecordBelongsToCurrentTenantProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantProducer()

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' which returns type '#testReturnType' which returns value for property '#testCurrentTenantIdProperty'" () {
        expect:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType)).getCreateScript() == expectedStatement

        where:
            testSchema              |   testFunctionName                        |   recordTableName     |   recordSchemaName    |   getCurrentTenantFunction    |   tenantColumnPair                                |   keyColumnsPairs                         || expectedStatement
            null                    |   "is_user_belongs_to_current_tenant"     |   "users"             |   null                |   "current_tenant()"          |   pairOfColumnWithType("tenant_id", "text")       |   [pairOfColumnWithType("id", "bigint")]  ||  "CREATE OR REPLACE FUNCTION get_current_tenant() RETURNS VARCHAR(255) AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
    }
}
