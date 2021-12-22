package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.context.IIdentifierValidator
import spock.lang.Specification

class FunctionDefinitionValidatorTest extends Specification {


    FunctionDefinitionValidator prepareSQLDefinitionsValidator(IIdentifierValidator... identifierValidators)
    {
        new FunctionDefinitionValidator(identifierValidators)
    }

    List<SQLDefinition> expectedSQLDefinition(String... names)
    {
        //TODO
        (List<SQLDefinition>)[Mock(IFunctionDefinition), Mock(IFunctionDefinition)]
    }

    List<SQLDefinition> ignoredSQLDefinition()
    {
        (List<SQLDefinition>)[Mock(SQLDefinition), Mock(SQLDefinition)]
    }

    def "should invoke validator for expected sql definitions objects"()
    {
        given:
            IIdentifierValidator identifierValidator = Mock(IIdentifierValidator)
            def tested = prepareSQLDefinitionsValidator(identifierValidator)
            Set<SQLDefinition> allDefinitions = new HashSet<>()
            def ignoredDefinitions = ignoredSQLDefinition()
            def expectedSQLDefinition = expectedSQLDefinition()
            allDefinitions.addAll(ignoredDefinitions)
            allDefinitions.addAll(expectedSQLDefinition)

        when:
            tested.validate(allDefinitions.toList())

        then:
            for (SQLDefinition sqlDefinition : ignoredDefinitions) {
                //TODO
                0 * identifierValidator.validate()
            }
    }
}
