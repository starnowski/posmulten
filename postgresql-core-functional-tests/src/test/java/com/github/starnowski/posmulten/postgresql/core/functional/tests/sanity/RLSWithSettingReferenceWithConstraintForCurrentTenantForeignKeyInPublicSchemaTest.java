package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSWithSettingReferenceWithConstraintForCurrentTenantForeignKeyInPublicSchemaTest extends AbstractRLSWithSettingReferenceWithConstraintForCurrentTenantForeignKeyTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
