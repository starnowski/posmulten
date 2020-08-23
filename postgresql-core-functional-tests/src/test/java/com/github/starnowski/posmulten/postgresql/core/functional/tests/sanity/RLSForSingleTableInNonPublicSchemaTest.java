package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSForSingleTableInNonPublicSchemaTest extends AbstractRLSForSingleTableTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
