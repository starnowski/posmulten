package com.github.starnowski.posmulten.postgresql.core

import org.jeasy.random.EasyRandom
import spock.lang.Specification

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.stream.Stream

import static java.util.stream.Collectors.toList

class DefaultFunctionDefinitionTest extends Specification {

    def "should create object based on passed object of type that implements IFunctionDefinition" ()
    {
        given:
            EasyRandom easyRandom = new EasyRandom()
            IFunctionDefinition passedObject = easyRandom.nextObject(DefaultFunctionDefinition)
            List<Method> publicMethods = returnPublicMethodsForInterface(IFunctionDefinition.class)

        when:
            def result = new DefaultFunctionDefinition(passedObject)

        then:
            result

        and: "all methods declared for interface IFunctionDefinition  for passed object and for the newly created object of type DefaultFunctionDefinition should return the same results"
            publicMethods.each {method ->
                Object resultValue =  method.invoke(result)
                Object passedObjectValue =  method.invoke(passedObject)
                assert Objects.equals(resultValue, passedObjectValue)
            }
    }

    protected Method[] returnPublicMethodsForInterface(Class aClass)
    {
        Stream.of(aClass.getDeclaredMethods()).filter({ method ->
            Modifier.isPublic(method.getModifiers())
        }).collect(toList())
    }
}
