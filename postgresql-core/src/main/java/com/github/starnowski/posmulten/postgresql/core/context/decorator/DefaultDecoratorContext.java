package com.github.starnowski.posmulten.postgresql.core.context.decorator;

import java.util.Map;

public class DefaultDecoratorContext implements BasicSharedSchemaContextDecoratorContext{
    @Override
    public Map<String, String> getReplaceCharactersMap() {
        return null;
    }
}
