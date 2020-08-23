package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSForSingleTableForWhichTenantColumnWasJustAddedInPublicSchemaTest extends AbstractRLSForSingleTableForWhichTenantColumnWasJustAddedTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
