package com.github.starnowski.posmulten.postgresql.core.functional.tests.combine;

public class RLSForSingleTableInNonPublicSchemaTest extends AbstractRLSForSingleTableTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
