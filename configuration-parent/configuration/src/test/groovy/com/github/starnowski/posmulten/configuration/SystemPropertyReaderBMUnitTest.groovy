package com.github.starnowski.posmulten.configuration

import com.github.starnowski.bmunit.extension.junit4.rule.BMUnitMethodRule
import org.jboss.byteman.contrib.bmunit.BMRule
import org.jboss.byteman.contrib.bmunit.BMUnitConfig
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

@BMUnitConfig(verbose = true)
class SystemPropertyReaderBMUnitTest extends Specification {

    @Rule
    public BMUnitMethodRule bmUnitMethodRule = new BMUnitMethodRule()

    def tested = new SystemPropertyReader()

    @Unroll
    @BMRule(name = "mocking method java.lang.System#getProperty(String)", targetClass = "com.github.starnowski.posmulten.configuration.SystemPropertyReader",
            targetMethod = "read(java.lang.String)", helper = "com.github.starnowski.posmulten.configuration.BMUnitHelperWithStaticStringProperty",
            targetLocation = "AFTER INVOKE java.lang.System.getProperty(java.lang.String)", action = "\$! = readProperty(\$1)")
    def "should return expected value #expectedValue for system property #property" ()
    {
        given:
            BMUnitHelperWithStaticStringProperty.resetMaps()
            BMUnitHelperWithStaticStringProperty.mockProperty(property, expectedValue)
            def expectedPropertiesThatWereRead = new HashMap()
            expectedPropertiesThatWereRead.put(property, 1)

        when:
            def result = tested.read(property)

        then:
            result == expectedValue

        and:
            BMUnitHelperWithStaticStringProperty.getMockedPropertiesReads() == expectedPropertiesThatWereRead

        where:
            property    || expectedValue
            "xxx"       || "this is value"
            "z"         || "test value"
    }
}