package com.github.starnowski.posmulten.postgresql.core.context.validators

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.context.IIdentifierValidator
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException
import spock.lang.Specification
import spock.lang.Unroll

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

    List<SQLDefinition> expectedSQLDefinitionWithFunctionReferences(List<String> functionReferences)
    {
        Set<SQLDefinition> mocks = new HashSet<>()
        for (String fr : functionReferences)
        {
            def mock = Mock(IFunctionDefinition)
            mock.getFunctionReference() >> fr
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
            IIdentifierValidator.ValidationResult dummyValidResult = new IIdentifierValidator.ValidationResult(true, "dummy result")

        when:
            tested.validate(allDefinitions.toList())

        then:
            for (String name : expectedNames()) {
                1 * identifierValidator1.validate(name) >> dummyValidResult
                1 * identifierValidator2.validate(name) >> dummyValidResult
            }
    }

    @Unroll
    def "should throw an exception for invalid sql definitions names [#invalidNames]"()
    {
        given:
            IIdentifierValidator identifierValidator1 = Mock(IIdentifierValidator)
            def tested = prepareSQLDefinitionsValidator(identifierValidator1)
            Set<SQLDefinition> allDefinitions = new HashSet<>()
            List<SQLDefinition> ignoredDefinitions = ignoredSQLDefinition()
            List<SQLDefinition> expectedSQLDefinition = expectedSQLDefinitionWithFunctionReferences(functionReferences)
            allDefinitions.addAll(ignoredDefinitions)
            allDefinitions.addAll(expectedSQLDefinition)
            IIdentifierValidator.ValidationResult dummyValidResult = new IIdentifierValidator.ValidationResult(true, "dummy result")

        when:
            tested.validate(allDefinitions.toList())

        then:
            for (String name : names) {
                if (!invalidNames.contains(name)) {
                    1 * identifierValidator1.validate(name) >> dummyValidResult
                }
            }
            for (String name : invalidNames) {
                1 * identifierValidator1.validate(name) >> new IIdentifierValidator.ValidationResult(false, "invalid name: " + name)
            }
        then:
            def ex = thrown(SharedSchemaContextBuilderException)

        and: "exception should have correct message"
            expectedMessageParts.every {
                ex.message.contains(it)
            }

        where:
            functionReferences                      |   names                           |   invalidNames            ||   expectedMessageParts
            ["XXX", "fun2"]                         |   ["XXX", "fun2"]                 |   ["XXX"]                 ||  ["Invalid identifier for function XXX:", "invalid name: XXX"]
            ["schema.x1", "fun", "public.fun1"]     |   ["x1", "fun", "fun1"]           |   ["fun1", "fun"]         ||  ["Invalid identifier for function fun:", "invalid name: fun", "Invalid identifier for function public.fun1:", "invalid name: fun1"]
    }
}
