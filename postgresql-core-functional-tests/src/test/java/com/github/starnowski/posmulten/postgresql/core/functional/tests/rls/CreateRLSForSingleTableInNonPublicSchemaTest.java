package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

public class CreateRLSForSingleTableInNonPublicSchemaTest extends AbstractCreateRLSForSingleTableTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
