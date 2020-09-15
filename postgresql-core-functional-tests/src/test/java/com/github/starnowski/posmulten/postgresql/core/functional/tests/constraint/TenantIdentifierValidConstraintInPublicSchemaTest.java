package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

public class TenantIdentifierValidConstraintInPublicSchemaTest extends AbstractTenantIdentifierValidConstraintTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
