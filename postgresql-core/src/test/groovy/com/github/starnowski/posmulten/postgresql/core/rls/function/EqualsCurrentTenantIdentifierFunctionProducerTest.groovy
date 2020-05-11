package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString

class EqualsCurrentTenantIdentifierFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new EqualsCurrentTenantIdentifierFunctionProducer()

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' which requires argument with type #testArgumentType and use the #testGetCurrentTenantIdFunction function" () {
        expect:
            tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, testArgumentType, { testGetCurrentTenantIdFunction })).getCreateScript() == exptectedStatement

        where:
            testSchema              |   testFunctionName            |   testGetCurrentTenantIdFunction      |   testArgumentType    || exptectedStatement
            null                    |   "is_current_tenant"         |   "get_current_tenant()"              |   null                ||   "CREATE OR REPLACE FUNCTION is_current_tenant(VARCHAR(255)) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "is_current_tenant"         |   "get_current_tenant()"              |   null                ||   "CREATE OR REPLACE FUNCTION public.is_current_tenant(VARCHAR(255)) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "non_public_schema"     |   "is_current_tenant"         |   "get_current_tenant()"              |   null                ||   "CREATE OR REPLACE FUNCTION non_public_schema.is_current_tenant(VARCHAR(255)) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            null                    |   "is_current_tenant"         |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION is_current_tenant(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "is_current_tenant"         |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION public.is_current_tenant(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "non_public_schema"     |   "is_current_tenant"         |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION non_public_schema.is_current_tenant(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            null                    |   "equal_cur_ten"             |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "equal_cur_ten"             |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION public.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "non_public_schema"     |   "equal_cur_ten"             |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION non_public_schema.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            null                    |   "equal_cur_ten"             |   "tenant()"                          |   "text"              ||   "CREATE OR REPLACE FUNCTION equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "equal_cur_ten"             |   "tenant()"                          |   "text"              ||   "CREATE OR REPLACE FUNCTION public.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "non_public_schema"     |   "equal_cur_ten"             |   "tenant()"                          |   "text"              ||   "CREATE OR REPLACE FUNCTION non_public_schema.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = tenant()\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
    }

    @Unroll
    def "should generate invocation of the function that determines if the passed tenant id belongs to the current tenant (#expectedInvocation) for schema #testSchema and for name #testFunctionName" () {
        expect:
            tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, null, { "get_current_tenant()" })).returnEqualsCurrentTenantIdentifierFunctionInvocation(passedFunctionArgument) == expectedInvocation

        where:
            testSchema              |   testFunctionName    |   passedFunctionArgument          ||  expectedInvocation
            null                    |   "is_current_tenant" |   forString("XXDFSAF")            ||  "is_current_tenant('XXDFSAF')"
            "public"                |   "is_current_tenant" |   forString("XXDFSAF")            ||  "public.is_current_tenant('XXDFSAF')"
            "non_public_schema"     |   "is_current_tenant" |   forString("XXDFSAF")            ||  "non_public_schema.is_current_tenant('XXDFSAF')"
            null                    |   "equal_cur_ten"     |   forString("XXDFSAF")            ||  "equal_cur_ten('XXDFSAF')"
            "public"                |   "equal_cur_ten"     |   forString("XXDFSAF")            ||  "public.equal_cur_ten('XXDFSAF')"
            "non_public_schema"     |   "equal_cur_ten"     |   forString("XXDFSAF")            ||  "non_public_schema.equal_cur_ten('XXDFSAF')"
            null                    |   "is_current_tenant" |   forString("GGGSQSF-hhh")        ||  "is_current_tenant('GGGSQSF-hhh')"
            "public"                |   "is_current_tenant" |   forString("GGGSQSF-hhh")        ||  "public.is_current_tenant('GGGSQSF-hhh')"
            "non_public_schema"     |   "is_current_tenant" |   forString("GGGSQSF-hhh")        ||  "non_public_schema.is_current_tenant('GGGSQSF-hhh')"
            null                    |   "equal_cur_ten"     |   forString("GGGSQSF-hhh")        ||  "equal_cur_ten('GGGSQSF-hhh')"
            "public"                |   "equal_cur_ten"     |   forString("GGGSQSF-hhh")        ||  "public.equal_cur_ten('GGGSQSF-hhh')"
            "non_public_schema"     |   "equal_cur_ten"     |   forString("GGGSQSF-hhh")        ||  "non_public_schema.equal_cur_ten('GGGSQSF-hhh')"
            null                    |   "is_current_tenant" |   forReference("\$1")             ||  "is_current_tenant(\$1)"
            "public"                |   "is_current_tenant" |   forReference("\$1")             ||  "public.is_current_tenant(\$1)"
            "non_public_schema"     |   "is_current_tenant" |   forReference("\$1")             ||  "non_public_schema.is_current_tenant(\$1)"
            null                    |   "equal_cur_ten"     |   forReference("\$1")             ||  "equal_cur_ten(\$1)"
            "public"                |   "equal_cur_ten"     |   forReference("\$1")             ||  "public.equal_cur_ten(\$1)"
            "non_public_schema"     |   "equal_cur_ten"     |   forReference("\$1")             ||  "non_public_schema.equal_cur_ten(\$1)"
            null                    |   "is_current_tenant" |   forReference("some_variable")   ||  "is_current_tenant(some_variable)"
            "public"                |   "is_current_tenant" |   forReference("some_variable")   ||  "public.is_current_tenant(some_variable)"
            "non_public_schema"     |   "is_current_tenant" |   forReference("some_variable")   ||  "non_public_schema.is_current_tenant(some_variable)"
            null                    |   "equal_cur_ten"     |   forReference("some_variable")   ||  "equal_cur_ten(some_variable)"
            "public"                |   "equal_cur_ten"     |   forReference("some_variable")   ||  "public.equal_cur_ten(some_variable)"
            "non_public_schema"     |   "equal_cur_ten"     |   forReference("some_variable")   ||  "non_public_schema.equal_cur_ten(some_variable)"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when argument type IGetCurrentTenantIdFunctionInvocationFactory is null, even if the rest of parameters are correct, function name #functionName, schema #testSchema"()
    {
        when:
            tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, testArgumentType, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Parameter of type IGetCurrentTenantIdFunctionInvocationFactory cannot be null"

        where:
            testSchema              |   testFunctionName            |   testArgumentType
            null                    |   "is_current_tenant"         |   null
            "public"                |   "is_current_tenant"         |   null
            "non_public_schema"     |   "is_current_tenant"         |   null
            null                    |   "is_current_tenant"         |   "text"
            "public"                |   "is_current_tenant"         |   "text"
            "non_public_schema"     |   "is_current_tenant"         |   "text"
            null                    |   "give_me_tenant"            |   "text"
            "public"                |   "give_me_tenant"            |   "text"
            "non_public_schema"     |   "give_me_tenant"            |   "text"
            null                    |   "give_me_tenant"            |   "VARCHAR(255)"
            "public"                |   "give_me_tenant"            |   "VARCHAR(255)"
            "non_public_schema"     |   "give_me_tenant"            |   "VARCHAR(255)"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when the argument type is blank, even if the rest of parameters are correct, function name #functionName, schema #testSchema"()
    {
        when:
            tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, testArgumentType, { testGetCurrentTenantIdFunction }))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Argument type cannot be blank"

        where:
            testSchema              |   testFunctionName            |   testGetCurrentTenantIdFunction  |   testArgumentType
            null                    |   "is_current_tenant"         |   "get_current_tenant()"          |   ""
            "public"                |   "is_current_tenant"         |   "get_current_tenant()"          |   ""
            "non_public_schema"     |   "is_current_tenant"         |   "get_current_tenant()"          |   ""
            null                    |   "is_current_tenant"         |   "tenant()"                      |   " "
            "public"                |   "is_current_tenant"         |   "tenant()"                      |   " "
            "non_public_schema"     |   "is_current_tenant"         |   "tenant()"                      |   " "
            null                    |   "give_me_tenant"            |   "tenant()"                      |   "      "
            "public"                |   "give_me_tenant"            |   "tenant()"                      |   "      "
            "non_public_schema"     |   "give_me_tenant"            |   "tenant()"                      |   "      "
    }

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(EqualsCurrentTenantIdentifierFunctionProducerParameters, constructorArgs: ["is_current_tenant", "public", "VARCHAR(32)", { "get_tenant()"} ])
    }
}
