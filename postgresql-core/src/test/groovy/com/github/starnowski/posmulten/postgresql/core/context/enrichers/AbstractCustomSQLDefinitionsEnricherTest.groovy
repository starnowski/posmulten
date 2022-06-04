package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import spock.lang.Specification

abstract class AbstractCustomSQLDefinitionsEnricherTest extends Specification {

    abstract List<String> getExpectedCreationScripts()

    abstract List<String> getIngoredCreationScripts()
}
