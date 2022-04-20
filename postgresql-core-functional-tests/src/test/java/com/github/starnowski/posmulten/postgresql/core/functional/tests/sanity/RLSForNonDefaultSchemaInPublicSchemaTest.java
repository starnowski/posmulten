package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSForNonDefaultSchemaInPublicSchemaTest extends AbstractRLSForNonDefaultSchemaTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
