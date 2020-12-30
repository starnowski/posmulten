package com.github.starnowski.posmulten.postgresql.core.util

import spock.lang.Specification
import spock.lang.Unroll

class PairTest extends Specification {

    @Unroll
    def "getLeft method should return first value passed in constructor #value"() {
        given:
            def right = "Value2"
            def pair = new Pair<String, String>(value, right)

        when:
            def result = pair.getLeft()

        then:
            result.is(value)

        where:
            value << ["value1", "XXXX", "", null]

    }

    @Unroll
    def "should return first value passed in constructor #value"() {
        given:
            def right = "Value2"
            def pair = new Pair<String, String>(value, right)

        when:
            def result = pair.getKey()

        then:
            result.is(value)

        where:
            value << ["value1", "XXXX", "", null]

    }

    @Unroll
    def "getLeft getRight should return second value passed in constructor #value"() {
        given:
            def left = "Value2"
            def pair = new Pair<String, String>(left, value)

        when:
            def result = pair.getRight()

        then:
            result.is(value)

        where:
            value << ["value1", "XXXX", "", null]

    }

    @Unroll
    def "should return second value passed in constructor #value"() {
        given:
            def left = "Value2"
            def pair = new Pair<String, String>(left, value)

        when:
            def result = pair.getValue()

        then:
            result.is(value)

        where:
            value << ["value1", "XXXX", "", null]

    }


    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1                                                             |   ob2
            new Pair<String, String>("XXX", "YYY")               |   new Pair<String, String>("XXX", "YYY")
            new Pair<String, String>("value1", "value2")         |   new Pair<String, String>("value1", "value2")
            new Pair<String, String>(null, "value2")             |   new Pair<String, String>(null, "value2")
            new Pair<String, String>(null, null)                 |   new Pair<String, String>(null, null)
            new Pair<String, String>("value1", null)             |   new Pair<String, String>("value1", null)
    }

    @Unroll
    def "toString method should return expected value (#expectedString) with key (#key) and value (#value)"()
    {
        given:
            def pair = new Pair(key,value)

        when:
            def result = pair.toString()

        then:
            result == expectedString

        where:
            key     |   value   | expectedString
            null    |   null    | "null=null"
            "X"     |   null    | "X=null"
            "X"     |   "Y"     | "X=Y"
            " "     |   "Y"     | " =Y"
            " "     |   " "     | " = "
            ""      |   "Y"     | "=Y"
    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1                                                             |   ob2
            new Pair<String, String>("XXX", "YYY")               |   new Pair<String, String>("XXX", "YYY")
            new Pair<String, String>("value1", "value2")         |   new Pair<String, String>("value1", "value2")
            new Pair<String, String>(null, "value2")             |   new Pair<String, String>(null, "value2")
            new Pair<String, String>(null, null)                 |   new Pair<String, String>(null, null)
            new Pair<String, String>("value1", null)             |   new Pair<String, String>("value1", null)
    }

    @Unroll
    def "equals method should return false for objects with different values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
        !ob1.equals(ob2) && !ob2.equals(ob1)

        where:
            ob1                                                         |   ob2
            new Pair<String, String>("XXX", "YYY")           |   new Pair<String, String>("ZZZ", "YYY")
            new Pair<String, String>("value1", "value2")     |   new Pair<String, String>("value1", "value3")
            new Pair<String, String>(null, "value2")         |   new Pair<String, String>(null, "value3")
            new Pair<String, String>("value1", null)         |   new Pair<String, String>("value3", null)
            new Pair<String, String>("value1", null)         |   new Pair<String, String>(null, null)
    }
}
