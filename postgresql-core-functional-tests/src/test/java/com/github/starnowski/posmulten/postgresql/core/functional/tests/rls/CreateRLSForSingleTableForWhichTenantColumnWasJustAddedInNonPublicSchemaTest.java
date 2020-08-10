package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

public class CreateRLSForSingleTableForWhichTenantColumnWasJustAddedInNonPublicSchemaTest extends AbstractCreateRLSForSingleTableForWhichTenantColumnWasJustAddedTest {
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
