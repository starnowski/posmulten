package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSForNonDefaultSchemaInNonPublicSchemaTest extends AbstractRLSForNonDefaultSchemaTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
