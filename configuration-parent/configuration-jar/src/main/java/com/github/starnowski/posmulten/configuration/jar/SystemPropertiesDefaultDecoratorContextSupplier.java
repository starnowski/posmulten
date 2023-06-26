package com.github.starnowski.posmulten.configuration.jar;

import com.github.starnowski.posmulten.configuration.SystemPropertyReader;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.starnowski.posmulten.configuration.jar.Constants.CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY;

public class SystemPropertiesDefaultDecoratorContextSupplier {

    private static String REPLACECHARACTERSMAP_PAIR_PATTERN = "(.*)=(.*)";
    private static Pattern replaceCharactersMapPattern = Pattern.compile(REPLACECHARACTERSMAP_PAIR_PATTERN);

    private final SystemPropertyReader systemPropertyReader;

    public SystemPropertiesDefaultDecoratorContextSupplier() {
        this(new SystemPropertyReader());
    }

    public SystemPropertiesDefaultDecoratorContextSupplier(SystemPropertyReader systemPropertyReader) {
        this.systemPropertyReader = systemPropertyReader;
    }

    public DefaultDecoratorContext get()
    {
        DefaultDecoratorContext.DefaultDecoratorContextBuilder decoratorContextBuilder = DefaultDecoratorContext.builder();
        String patternsValue = systemPropertyReader.read(CONTEXT_DECORATOR_REPLACECHARACTERSMAP_PROPERTY);
        if (patternsValue != null) {
            String[] patterns = patternsValue.split(",");
            Map<String, String> replaceCharactersMap = new HashMap<>();
            for (String patternPari : patterns) {
                Matcher m = replaceCharactersMapPattern.matcher(patternPari);
                if (m.find()) {
                    replaceCharactersMap.put(m.group(1), m.group(2));
                }
            }
            decoratorContextBuilder.withReplaceCharactersMap(replaceCharactersMap);
        }
        return decoratorContextBuilder.build();
    }
}
