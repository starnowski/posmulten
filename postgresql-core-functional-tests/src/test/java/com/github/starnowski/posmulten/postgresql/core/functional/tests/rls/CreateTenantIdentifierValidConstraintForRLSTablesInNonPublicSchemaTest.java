package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

public class CreateTenantIdentifierValidConstraintForRLSTablesInNonPublicSchemaTest extends AbstractCreateTenantIdentifierValidConstraintForRLSTablesTest {
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
