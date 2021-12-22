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

    List<String> expectedNames()
    {
        Arrays.asList("fun1", "my_fun", "your_function")
    }

    List<SQLDefinition> expectedSQLDefinition(String... names)
    {
        Set<SQLDefinition> mocks = new HashSet<>()
        for (int i = 0; i < names.length; i++)
        {
            def mock = Mock(IFunctionDefinition)
            mock.getFunctionReference() >> (i % 2 == 0 ? names[i] : "some_schema." + names[i])
            mocks.add(mock)
        }
        mocks.toList()
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
            def expectedSQLDefinition = expectedSQLDefinition(expectedNames())
            allDefinitions.addAll(ignoredDefinitions)
            allDefinitions.addAll(expectedSQLDefinition)

        when:
            tested.validate(allDefinitions.toList())

        then:
            for (String name : expectedNames()) {
                1 * identifierValidator.validate(name)
            }
    }
}
