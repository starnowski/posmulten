package com.github.starnowski.posmulten.configuration;

import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BMUnitHelperWithStaticStringProperty extends Helper {

    private static Map<String, String> mockedProperties = new HashMap<>();
    private static Map<String, Integer> mockedPropertiesReads = new HashMap<>();

    protected BMUnitHelperWithStaticStringProperty(Rule rule) {
        super(rule);
    }

    public String readProperty(String property) {
        Integer value = mockedPropertiesReads.computeIfAbsent(property, (key) -> new Integer(0));
        mockedPropertiesReads.put(property, ++value);
        return mockedProperties.get(property);
    }

    public static Map<String, Integer> getMockedPropertiesReads() {
        return Collections.unmodifiableMap(mockedPropertiesReads);
    }

    public static void mockProperty(String property, String value){
        mockedProperties.put(property, value);
    }

    public static void resetMaps()
    {
        mockedProperties.clear();
        mockedPropertiesReads.clear();
    }
}