package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

class DefaultSharedSchemaContextBuilderConfigurationInitializingBeanTest extends Specification {

    def tested = new DefaultSharedSchemaContextBuilderConfigurationInitializingBean()

    @Unroll
    def "should return builder for schema '#schema'"()
    {
        given:
            def configuration = new SharedSchemaContextConfiguration().setDefaultTenantIdColumn(schema)

        when:
            def result = tested.produce(configuration)

        then:
            result
            result.getSharedSchemaContextRequestCopy().getDefaultSchema() == schema

        where:
            schema << [null, "public", "some_schema", "other_than_public"]
    }
}
