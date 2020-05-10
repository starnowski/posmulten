package com.github.starnowski.posmulten.postgresql.core.common.function

import com.github.starnowski.posmulten.postgresql.core.RandomString
import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.api.Randomizer
import org.junit.Assert
import spock.lang.Specification

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.stream.Stream

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentBuilder.forType
import static java.util.stream.Collectors.toList

class DefaultFunctionDefinitionTest extends Specification {

    def "should create object based on passed object of type that implements IFunctionDefinition" ()
    {
        given:
            RandomString randomString = new RandomString(12, new Random(), RandomString.lower)
            EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
                    .randomize(IFunctionArgument.class, new Randomizer<IFunctionArgument>() {

                IFunctionArgument getRandomValue() {
                    forType(randomString.nextString())
                }
            })
            EasyRandom easyRandom = new EasyRandom(easyRandomParameters)
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
                Assert.assertEquals("values for method " + method.getName() + " does not match", resultValue, passedObjectValue)
            }
    }

    protected Method[] returnPublicMethodsForInterface(Class aClass)
    {
        Stream.of(aClass.getDeclaredMethods()).filter({ method ->
            Modifier.isPublic(method.getModifiers())
        }).collect(toList())
    }
}
