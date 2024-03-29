package com.github.starnowski.posmulten.configuration.jar

import com.github.starnowski.posmulten.configuration.SystemPropertyReader
import spock.lang.Specification
import spock.lang.Unroll
import static com.github.starnowski.posmulten.configuration.jar.Constants.CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY
import static com.github.starnowski.posmulten.configuration.jar.Constants.CONTEXT_DECORATOR_REPLACECHARACTERSMAP_SEPARATOR_PROPERTY

class SystemPropertiesDefaultDecoratorContextSupplierTest extends Specification {

    @Unroll
    def "should supply decorator context with expected #replaceCharactersMap parameters when system property has value #systemProperty"(){
        given:
            SystemPropertyReader systemPropertyReader = Mock(SystemPropertyReader)
            def tested = new SystemPropertiesDefaultDecoratorContextSupplier(systemPropertyReader)

        when:
            def result = tested.get()

        then:
            1 * systemPropertyReader.read(CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY) >> systemProperty
            result.getReplaceCharactersMap() == replaceCharactersMap

        where:
            systemProperty                                              ||  replaceCharactersMap
            "dbname=some_public_db"                                     ||  [dbname: "some_public_db"]
            "dbname=some_public_db,grantee=user"                        ||  [dbname: "some_public_db", grantee: "user"]
            "{{schema}}=non_public_schema,{{grantee}}=johnDoe"          ||  ["{{schema}}": "non_public_schema", "{{grantee}}": "johnDoe"]
            "\$schema=non_public_schema,-->user<--=johnDoe,ro;le=admin" ||  ["\$schema": "non_public_schema", "-->user<--": "johnDoe", "ro;le": "admin"]
    }

    @Unroll
    def "should supply decorator context with expected #replaceCharactersMap parameters when system property has value #systemProperty with separator #separator"(){
        given:
            SystemPropertyReader systemPropertyReader = Mock(SystemPropertyReader)
            def tested = new SystemPropertiesDefaultDecoratorContextSupplier(systemPropertyReader)

        when:
            def result = tested.get()

        then:
            1 * systemPropertyReader.read(CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY) >> systemProperty
            1 * systemPropertyReader.read(CONTEXT_DECORATOR_REPLACECHARACTERSMAP_SEPARATOR_PROPERTY) >> separator
            result.getReplaceCharactersMap() == replaceCharactersMap

        where:
            systemProperty                                                      |   separator   ||  replaceCharactersMap
            "dbname=some_public_db;grantee=user"                                |";"            ||  [dbname: "some_public_db", grantee: "user"]
            "dbname=some_public_db.grantee=user"                                |"\\."            ||  [dbname: "some_public_db", grantee: "user"]
            "{{schema}}=non_public_schema,{{grantee}}=johnDoe"                  |","            ||  ["{{schema}}": "non_public_schema", "{{grantee}}": "johnDoe"]
            "\$schema=non_public_schemasep-->user<--=johnDoesepro;le=admin"     |"sep"          ||  ["\$schema": "non_public_schema", "-->user<--": "johnDoe", "ro;le": "admin"]
            "\$schema=non_public_schema;sep-->user<--=johnDoe;sepro;le=admin"   |"sep"          ||  ["\$schema": "non_public_schema;", "-->user<--": "johnDoe;", "ro;le": "admin"]
    }
}
