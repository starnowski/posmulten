package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

public class TenantIdentifierValidConstraintInNonPublicSchemaTest extends AbstractTenantIdentifierValidConstraintTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
