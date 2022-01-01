package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import spock.lang.Specification
import spock.lang.Unroll

class CurrentTenantIdPropertyTypeEnricherTest extends Specification {

    def tested = new CurrentTenantIdPropertyTypeEnricher()

    @Unroll
    def "should set currentTenantIdPropertyType property, expected value #expected"()
    {
        given:
            def context = new SharedSchemaContext();

        when:
            def result = tested.enrich(context, request)

        then:
            result.currentTenantIdPropertyType == expected

        and: "enricher should return the same context object (same instance)"
            result.is(context)

        where:
            request                             ||  expected
            new SharedSchemaContextRequest()    ||  "VARCHAR(255)"
            request("VARCHAR(255)")             ||  "VARCHAR(255)"
            request("VARCHAR(32)")              ||  "VARCHAR(32)"
            request("string")                   ||  "string"
            request("INT")                      ||  "INT"
    }

    SharedSchemaContextRequest request(String type)
    {
        def result = new SharedSchemaContextRequest()
        result.setCurrentTenantIdPropertyType(type)
        result
    }
}
