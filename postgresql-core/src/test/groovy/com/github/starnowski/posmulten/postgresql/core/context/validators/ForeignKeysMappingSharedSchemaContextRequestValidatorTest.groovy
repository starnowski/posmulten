package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import spock.lang.Specification

class ForeignKeysMappingSharedSchemaContextRequestValidatorTest extends Specification {

    def tested = new ForeignKeysMappingSharedSchemaContextRequestValidator()

    def "should not throw any exception when foreign keys mapping is correctly defined"()
    {
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.createSameTenantConstraintForForeignKey("comments", "users", [user_id: id], null)
            builder.createRLSPolicyForTable()
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.getSameTenantConstraintForForeignKeyProperties().pu
        //request.getSameTenantConstraintForForeignKeyProperties()

    }
}
