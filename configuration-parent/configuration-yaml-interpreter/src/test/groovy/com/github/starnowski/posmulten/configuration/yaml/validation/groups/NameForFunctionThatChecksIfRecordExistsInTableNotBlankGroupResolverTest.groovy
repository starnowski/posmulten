package com.github.starnowski.posmulten.configuration.yaml.validation.groups

import com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

class NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolverTest extends Specification {

    @Unroll
    def "should return group validation class when createForeignKeyConstraintWithTenantColumn is #value"(){
        given:
            def tested = new NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver()
            def sharedContext = new SharedSchemaContextConfiguration().setCreateForeignKeyConstraintWithTenantColumn(value)

        when:
            def result = tested.resolveForSharedSchemaContextConfiguration(sharedContext, null)

        then:
            result == PrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank

        where:
            value << [null, false]
    }

    @Unroll
    def "should return null as group validation class when createForeignKeyConstraintWithTenantColumn is true"(){
        given:
            def tested = new NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver()
            def sharedContext = new SharedSchemaContextConfiguration().setCreateForeignKeyConstraintWithTenantColumn(true)

        when:
            def result = tested.resolveForSharedSchemaContextConfiguration(sharedContext, null)

        then:
            result == null
    }
}
