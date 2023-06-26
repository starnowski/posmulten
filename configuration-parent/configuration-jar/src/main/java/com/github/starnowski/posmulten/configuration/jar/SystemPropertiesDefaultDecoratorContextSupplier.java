package com.github.starnowski.posmulten.configuration.jar;

import com.github.starnowski.posmulten.configuration.SystemPropertyReader;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;

public class SystemPropertiesDefaultDecoratorContextSupplier {

    private final SystemPropertyReader systemPropertyReader;

    public SystemPropertiesDefaultDecoratorContextSupplier() {
        this(new SystemPropertyReader());
    }

    public SystemPropertiesDefaultDecoratorContextSupplier(SystemPropertyReader systemPropertyReader) {
        this.systemPropertyReader = systemPropertyReader;
    }

    public DefaultDecoratorContext get()
    {
        return null;
    }
}
