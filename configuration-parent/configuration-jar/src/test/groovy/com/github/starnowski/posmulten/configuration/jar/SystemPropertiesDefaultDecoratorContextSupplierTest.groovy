package com.github.starnowski.posmulten.configuration.jar

import com.github.starnowski.posmulten.configuration.SystemPropertyReader
import spock.lang.Specification
import spock.lang.Unroll

class SystemPropertiesDefaultDecoratorContextSupplierTest extends Specification {

    @Unroll
    def "should supply decorator context with expected #replaceCharactersMap parameters when system property has value #systemProperty"(){
        given:
            SystemPropertyReader systemPropertyReader = Mock(SystemPropertyReader)
            def tested = new SystemPropertiesDefaultDecoratorContextSupplier(systemPropertyReader)

        when:
            def result = tested.get()

        then:

    }
}
