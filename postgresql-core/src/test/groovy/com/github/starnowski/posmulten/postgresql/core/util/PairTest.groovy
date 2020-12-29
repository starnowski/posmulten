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
            value << ["value1", "XXXX", ""]

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
            value << ["value1", "XXXX", ""]

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
            value << ["value1", "XXXX", ""]

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
            value << ["value1", "XXXX", ""]

    }


    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1                                                         |   ob2
            new Pair<String, String>("XXX", "YYY")           |   new Pair<String, String>("XXX", "YYY")
            new Pair<String, String>("value1", "value2")     |   new Pair<String, String>("value1", "value2")
    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1                                                         |   ob2
            new Pair<String, String>("XXX", "YYY")           |   new Pair<String, String>("XXX", "YYY")
            new Pair<String, String>("value1", "value2")     |   new Pair<String, String>("value1", "value2")
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
    }

    @Unroll
    def "should throw exception when null parameter is passed left: #left right: #right"()
    {
        when:
            new Pair<String, String>(left, right)

        then:
            def ex = thrown(NullPointerException.class)
            ex

        where:
            left    |   right
            null    |   "XXX"
            "XXX"   |   null
            null    |   null
    }
}
