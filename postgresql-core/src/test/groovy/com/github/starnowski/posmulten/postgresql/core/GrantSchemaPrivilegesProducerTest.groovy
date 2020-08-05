package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class GrantSchemaPrivilegesProducerTest extends Specification {

    def tested = new GrantSchemaPrivilegesProducer()

    @Unroll
    def "should return granting access statement '#expectedStatement' for schema '#schema' and user '#policyTargetUsername' with specified privileges '#privileges'" () {
        expect:
            tested.produce(schema, policyTargetUsername, privileges).getCreateScript() == expectedStatement

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
    def "should return revoking access statement '#expectedStatement' for schema '#schema' and user '#policyTargetUsername' with specified privileges '#privileges'" () {
        expect:
        tested.produce(schema, policyTargetUsername, privileges).getDropScript() == expectedStatement

        where:
        privileges	        |	schema	    | policyTargetUsername  ||	expectedStatement
        ["ALL"]	            |	"public"	| "pos-user"	        ||	"REVOKE ALL ON SCHEMA public FROM \"pos-user\";"
        ["ALL PRIVILEGES"]	|	"public"	| "pos-user"	        ||	"REVOKE ALL PRIVILEGES ON SCHEMA public FROM \"pos-user\";"
        ["CREATE"]	        |	"public"	| "pos-user"	        ||	"REVOKE CREATE ON SCHEMA public FROM \"pos-user\";"
        ["USAGE"]	        |	"public"	| "pos-user"	        ||	"REVOKE USAGE ON SCHEMA public FROM \"pos-user\";"
        ["CREATE", "USAGE"]	|	"public"	| "pos-user"	        ||	"REVOKE CREATE, USAGE ON SCHEMA public FROM \"pos-user\";"
        ["USAGE", "CREATE"]	|	"public"	| "pos-user"	        ||	"REVOKE USAGE, CREATE ON SCHEMA public FROM \"pos-user\";"
        // other schema
        ["ALL"]	            |	"secondary"	| "pos-user"	        ||	"REVOKE ALL ON SCHEMA secondary FROM \"pos-user\";"
        ["ALL PRIVILEGES"]	|	"secondary"	| "pos-user"	        ||	"REVOKE ALL PRIVILEGES ON SCHEMA secondary FROM \"pos-user\";"
        ["CREATE"]	        |	"secondary"	| "pos-user"	        ||	"REVOKE CREATE ON SCHEMA secondary FROM \"pos-user\";"
        ["USAGE"]	        |	"secondary"	| "pos-user"	        ||	"REVOKE USAGE ON SCHEMA secondary FROM \"pos-user\";"
        ["CREATE", "USAGE"]	|	"secondary"	| "pos-user"	        ||	"REVOKE CREATE, USAGE ON SCHEMA secondary FROM \"pos-user\";"
        ["USAGE", "CREATE"]	|	"secondary"	| "pos-user"	        ||	"REVOKE USAGE, CREATE ON SCHEMA secondary FROM \"pos-user\";"
        // other user
        ["ALL"]	            |	"secondary"	| "john-doe"	        ||	"REVOKE ALL ON SCHEMA secondary FROM \"john-doe\";"
        ["ALL PRIVILEGES"]	|	"secondary"	| "john-doe"	        ||	"REVOKE ALL PRIVILEGES ON SCHEMA secondary FROM \"john-doe\";"
        ["CREATE"]	        |	"secondary"	| "john-doe"	        ||	"REVOKE CREATE ON SCHEMA secondary FROM \"john-doe\";"
        ["USAGE"]	        |	"secondary"	| "john-doe"	        ||	"REVOKE USAGE ON SCHEMA secondary FROM \"john-doe\";"
        ["CREATE", "USAGE"]	|	"secondary"	| "john-doe"	        ||	"REVOKE CREATE, USAGE ON SCHEMA secondary FROM \"john-doe\";"
        ["USAGE", "CREATE"]	|	"secondary"	| "john-doe"	        ||	"REVOKE USAGE, CREATE ON SCHEMA secondary FROM \"john-doe\";"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when schema name is null, no matter if user name \"#user\" or privileges \"#privileges\" are correct"()
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
    def "should throw exception of type 'IllegalArgumentException' when schema name is blank \"#schema\" , no matter if user name \"#user\" or privileges \"#privileges\" are correct"()
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
    def "should throw exception of type 'IllegalArgumentException' when user is null, no matter if schema name \"#schema\" or privileges \"#privileges\" are correct"()
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
    def "should throw exception of type 'IllegalArgumentException' when user is blank, no matter if schema name \"#schema\" or privileges \"#privileges\" are correct"()
    {
        when:
            tested.produce(schema, user, privileges)

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

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when privileges list is null, no matter if schema name \"#schema\" or user name \"#user\" are correct"()
    {
        when:
            tested.produce(schema, user, null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "privileges list cannot be null"

        where:
            schema          |   user
            "public"        |   "pos-user"
            "public"        |   "owner"
            "non_public"    |   "owner"
            "non_public"    |   "pos-user"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when privileges list is empty, no matter if schema name \"#schema\" or user name \"#user\" are correct"()
    {
        when:
            tested.produce(schema, user, new ArrayList<String>())

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "privileges list cannot be empty"

        where:
            schema          |   user
            "public"        |   "pos-user"
            "public"        |   "owner"
            "non_public"    |   "owner"
            "non_public"    |   "pos-user"
    }
}
