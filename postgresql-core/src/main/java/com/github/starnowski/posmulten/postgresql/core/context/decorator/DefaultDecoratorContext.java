package com.github.starnowski.posmulten.postgresql.core.context.decorator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class DefaultDecoratorContext implements BasicSharedSchemaContextDecoratorContext {

    private final Map<String, String> replaceCharactersMap;

    public DefaultDecoratorContext(Map<String, String> replaceCharactersMap) {
        this.replaceCharactersMap = unmodifiableMap(ofNullable(replaceCharactersMap).orElse(new HashMap<>()));
    }

    @Override
    public Map<String, String> getReplaceCharactersMap() {
        return replaceCharactersMap;
    }

    public static DefaultDecoratorContextBuilder builder() {
        return new DefaultDecoratorContextBuilder();
    }

    public static class DefaultDecoratorContextBuilder {
        private Map<String, String> replaceCharactersMap;

        public DefaultDecoratorContextBuilder withReplaceCharactersMap(Map<String, String> replaceCharactersMap) {
            this.replaceCharactersMap = replaceCharactersMap;
            return this;
        }

        public DefaultDecoratorContext build() {
            return new DefaultDecoratorContext(this.replaceCharactersMap);
        }
    }
}
