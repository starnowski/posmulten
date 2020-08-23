package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSForSingleTableForWhichTenantColumnWasJustAddedInNonPublicSchemaTest extends AbstractRLSForSingleTableForWhichTenantColumnWasJustAddedTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
