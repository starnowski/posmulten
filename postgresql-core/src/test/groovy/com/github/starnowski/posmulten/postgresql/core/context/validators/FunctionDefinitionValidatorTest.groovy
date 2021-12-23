package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.context.IIdentifierValidator
import spock.lang.Specification

import static java.util.Arrays.asList

class FunctionDefinitionValidatorTest extends Specification {

    FunctionDefinitionValidator prepareSQLDefinitionsValidator(IIdentifierValidator... identifierValidators)
    {
        new FunctionDefinitionValidator(asList(identifierValidators))
    }

    List<String> expectedNames()
    {
        asList("fun1", "my_fun", "your_function", "", "")
    }

    List<SQLDefinition> expectedSQLDefinition(List<String> names)
    {
        Set<SQLDefinition> mocks = new HashSet<>()
        for (int i = 0; i < names.size(); i++)
        {
            def mock = Mock(IFunctionDefinition)
            mock.getFunctionReference() >> (i % 2 == 0 ? names.get(i) : "some_schema." + names.get(i))
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
            IIdentifierValidator identifierValidator1 = Mock(IIdentifierValidator)
            IIdentifierValidator identifierValidator2 = Mock(IIdentifierValidator)
            def tested = prepareSQLDefinitionsValidator(identifierValidator1, identifierValidator2)
            Set<SQLDefinition> allDefinitions = new HashSet<>()
            List<SQLDefinition> ignoredDefinitions = ignoredSQLDefinition()
            List<SQLDefinition> expectedSQLDefinition = expectedSQLDefinition(expectedNames())
            allDefinitions.addAll(ignoredDefinitions)
            allDefinitions.addAll(expectedSQLDefinition)

        when:
            tested.validate(allDefinitions.toList())

        then:
            for (String name : expectedNames()) {
                1 * identifierValidator1.validate(name)
                1 * identifierValidator2.validate(name)
            }
    }
}
