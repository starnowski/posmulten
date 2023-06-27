package com.github.starnowski.posmulten.configuration;

import static java.lang.System.getProperty;

/**
 * The facade that invokes static method {@link System#getProperty(String)}
 */
public class SystemPropertyReader {
    /**
     * Invokes {@link System#getProperty(String)} method for passed property
     * @param property for which component should read value
     * @return check method {@link System#getProperty(String)}
     */
    public String read(String property) {
        return getProperty(property);
    }
}
