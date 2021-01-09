package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import spock.lang.Specification

class DefaultSharedSchemaContextBuilderFactoryTest extends Specification {

    def "should create builder component bases on aggregated components"()
    {
        given:
            def defaultSharedSchemaContextBuilderConfigurationEnricher = Mock(DefaultSharedSchemaContextBuilderConfigurationEnricher)
            def defaultSharedSchemaContextBuilderConfigurationInitializingBean = Mock(DefaultSharedSchemaContextBuilderConfigurationInitializingBean)
            def tested = new DefaultSharedSchemaContextBuilderFactory(defaultSharedSchemaContextBuilderConfigurationEnricher, defaultSharedSchemaContextBuilderConfigurationInitializingBean)
            def builder = Mock(DefaultSharedSchemaContextBuilder)
            def configuration = Mock(SharedSchemaContextConfiguration)

        when:
            def result = tested.build(configuration)

        then:
            result == builder
            1 * defaultSharedSchemaContextBuilderConfigurationInitializingBean.produce(configuration) >> builder

        and: "after builder initialization enrich it"
            1 * defaultSharedSchemaContextBuilderConfigurationEnricher.enrich(builder, configuration) >> builder

        and: "do not invoke builder component"
            0 * builder._
    }

    def "should use correct components"()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilderFactory()

        when:
            def defaultSharedSchemaContextBuilderConfigurationEnricher = tested.getDefaultSharedSchemaContextBuilderConfigurationEnricher()
            def defaultSharedSchemaContextBuilderConfigurationInitializingBean = tested.getDefaultSharedSchemaContextBuilderConfigurationInitializingBean()

        then:
            defaultSharedSchemaContextBuilderConfigurationEnricher.getClass().equals(DefaultSharedSchemaContextBuilderConfigurationEnricher.class)
            defaultSharedSchemaContextBuilderConfigurationInitializingBean.getClass().equals(DefaultSharedSchemaContextBuilderConfigurationInitializingBean.class)
    }
}
