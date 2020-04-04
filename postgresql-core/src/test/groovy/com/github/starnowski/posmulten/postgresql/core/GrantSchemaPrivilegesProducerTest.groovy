package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class GrantSchemaPrivilegesProducerTest extends Specification {

    def tested = new GrantSchemaPrivilegesProducer()

    @Unroll
    def "should return granting access statement '#expectedStatement' for schema '#schema' and user '#policyTargetUsername' with specified privileges '#privileges'" () {
        expect:
            tested.produce(schema, policyTargetUsername, privileges) == expectedStatement

        where:
            privileges	        |	schema	    | policyTargetUsername  ||	expectedStatement
            ["ALL"]	            |	"public"	| "pos-user"	        ||	"GRANT ALL ON SCHEMA public TO \"pos-user\";"
            ["ALL PRIVILEGES"]	|	"public"	| "pos-user"	        ||	"GRANT ALL PRIVILEGES ON SCHEMA public TO \"pos-user\";"
            ["CREATE"]	        |	"public"	| "pos-user"	        ||	"GRANT CREATE ON SCHEMA public TO \"pos-user\";"
            ["USAGE"]	        |	"public"	| "pos-user"	        ||	"GRANT USAGE ON SCHEMA public TO \"pos-user\";"
            ["CREATE", "USAGE"]	|	"public"	| "pos-user"	        ||	"GRANT CREATE, USAGE ON SCHEMA public TO \"pos-user\";"
            ["USAGE", "CREATE"]	|	"public"	| "pos-user"	        ||	"GRANT USAGE, CREATE ON SCHEMA public TO \"pos-user\";"
            // other schema
            ["ALL"]	            |	"secondary"	| "pos-user"	        ||	"GRANT ALL ON SCHEMA secondary TO \"pos-user\";"
            ["ALL PRIVILEGES"]	|	"secondary"	| "pos-user"	        ||	"GRANT ALL PRIVILEGES ON SCHEMA secondary TO \"pos-user\";"
            ["CREATE"]	        |	"secondary"	| "pos-user"	        ||	"GRANT CREATE ON SCHEMA secondary TO \"pos-user\";"
            ["USAGE"]	        |	"secondary"	| "pos-user"	        ||	"GRANT USAGE ON SCHEMA secondary TO \"pos-user\";"
            ["CREATE", "USAGE"]	|	"secondary"	| "pos-user"	        ||	"GRANT CREATE, USAGE ON SCHEMA secondary TO \"pos-user\";"
            ["USAGE", "CREATE"]	|	"secondary"	| "pos-user"	        ||	"GRANT USAGE, CREATE ON SCHEMA secondary TO \"pos-user\";"
            // other user
            ["ALL"]	            |	"secondary"	| "john-doe"	        ||	"GRANT ALL ON SCHEMA secondary TO \"john-doe\";"
            ["ALL PRIVILEGES"]	|	"secondary"	| "john-doe"	        ||	"GRANT ALL PRIVILEGES ON SCHEMA secondary TO \"john-doe\";"
            ["CREATE"]	        |	"secondary"	| "john-doe"	        ||	"GRANT CREATE ON SCHEMA secondary TO \"john-doe\";"
            ["USAGE"]	        |	"secondary"	| "john-doe"	        ||	"GRANT USAGE ON SCHEMA secondary TO \"john-doe\";"
            ["CREATE", "USAGE"]	|	"secondary"	| "john-doe"	        ||	"GRANT CREATE, USAGE ON SCHEMA secondary TO \"john-doe\";"
            ["USAGE", "CREATE"]	|	"secondary"	| "john-doe"	        ||	"GRANT USAGE, CREATE ON SCHEMA secondary TO \"john-doe\";"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when schema name is null, no matter if column name \"#user\" or column type \"#privileges\" are correct"()
    {
        when:
            tested.produce(null, user, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Schema name cannot be null"

        where:
            user                |   privileges
            "pos-user"          |   ["USAGE"]
            "pos-user"          |   ["CREATE"]
            "pos-user"          |   ["CREATE", "USAGE"]
            "pos-user"          |   ["ALL"]
            "pos-user"          |   ["ALL PRIVILEGES"]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when schema name is blank \"#schema\" , no matter if column name \"#user\" or column type \"#privileges\" are correct"()
    {
        when:
            tested.produce(schema, user, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Schema name cannot be blank"

        where:
            user                |   privileges          |   schema
            "pos-user"          |   ["USAGE"]           |   ""
            "pos-user"          |   ["USAGE"]           |   "     "
            "pos-user"          |   ["CREATE"]          |   ""
            "pos-user"          |   ["CREATE"]          |   "     "
            "pos-user"          |   ["CREATE", "USAGE"] |   ""
            "pos-user"          |   ["CREATE", "USAGE"] |   "         "
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when user is null, no matter if column name \"#schema\" or column type \"#privileges\" are correct"()
    {
        when:
            tested.produce(schema, null, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "user cannot be null"

        where:
            schema          |   privileges
            "public"        |   ["USAGE"]
            "public"        |   ["CREATE"]
            "non_public"    |   ["CREATE", "USAGE"]
            "schema1"       |   ["ALL"]
            "non_public"    |   ["ALL PRIVILEGES"]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when user is blank, no matter if column name \"#schema\" or column type \"#privileges\" are correct"()
    {
        when:
            tested.produce(schema, null, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "user cannot be blank"

        where:
            schema          |   privileges          |   user
            "public"        |   ["USAGE"]           |   ""
            "public"        |   ["CREATE"]          |   "      "
            "non_public"    |   ["CREATE", "USAGE"] |   ""
            "schema1"       |   ["ALL"]             |   "  "
            "non_public"    |   ["ALL PRIVILEGES"]  |   "              "
    }
}
