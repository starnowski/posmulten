package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

public class CreateRLSForSingleTableForWhichTenantColumnWasJustAddedInPublicSchemaTest extends AbstractCreateRLSForSingleTableForWhichTenantColumnWasJustAddedTest {
    @Override
    protected String getSchema() {
        return null;
    }
}
