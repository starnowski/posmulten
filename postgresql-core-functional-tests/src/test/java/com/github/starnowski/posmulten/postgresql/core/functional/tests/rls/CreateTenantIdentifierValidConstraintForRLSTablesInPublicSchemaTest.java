package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

public class CreateTenantIdentifierValidConstraintForRLSTablesInPublicSchemaTest extends AbstractCreateTenantIdentifierValidConstraintForRLSTablesTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
