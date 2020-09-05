package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import spock.lang.Specification

class ForeignKeysMappingSharedSchemaContextRequestValidatorTest extends Specification {

    def tested = new ForeignKeysMappingSharedSchemaContextRequestValidator()

    def "should not throw any exception when foreign keys mapping is correctly defined for tests parameters: #foreignKeysTable, #primaryKeysTable"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.createSameTenantConstraintForForeignKey(foreignKeysTable, primaryKeysTable, [user_id: "id"], null)
            builder.createRLSPolicyForTable(primaryKeysTable, [id: null], null, null)
            SharedSchemaContextRequest request = builder.getSharedSchemaContextRequestCopy()

        when:
            tested.validate(request)

        then:
            noExceptionThrown()

        where:
            foreignKeysTable    |   primaryKeysTable
            "comments"          |   "users"
    }
}
