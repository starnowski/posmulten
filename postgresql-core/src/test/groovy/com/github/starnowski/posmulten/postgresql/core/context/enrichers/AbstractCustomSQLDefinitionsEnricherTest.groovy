package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.util.Pair
import spock.lang.Specification

abstract class AbstractCustomSQLDefinitionsEnricherTest<T extends AbstractCustomSQLDefinitionsEnricher> extends Specification {

    def "should select correct custom definitions"(){
        given:
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.setGrantee("XXX")
            def testData = getTestData()
            testData.forEach({it -> builder.addCustomSQLDefinition(it.getKey(), it.getValue())})
            def request = builder.getSharedSchemaContextRequestCopy()
            ISharedSchemaContext context = new SharedSchemaContext()
            T tested = getTestedObject()

        when:
            def result = tested.enrich(context, request)

        then:
            result.getSqlDefinitions().createScript == getExpectedCreationScripts()

        and: "test data should contains values that should be ignored"
            testData.containsAll(getIgnoredCreationScripts())

        and: "collection of ignored values can not be empty"
            !getIgnoredCreationScripts().isEmpty()
    }

    abstract T getTestedObject()

    abstract List<Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>> getTestData()

    abstract List<String> getExpectedCreationScripts()

    abstract List<String> getIgnoredCreationScripts()

    protected static pp(String position) {
        return new ConstantCustomSQLDefinitionPairPositionProvider(position)
    }

    protected static class ConstantCustomSQLDefinitionPairPositionProvider implements CustomSQLDefinitionPairPositionProvider {

        private String position

        protected ConstantCustomSQLDefinitionPairPositionProvider(String position){
            this.position = position
        }

        @Override
        String getPosition() {
            position
        }
    }
}
