package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSWithSettingReferenceWithConstraintForCurrentTenantForeignKeyInNonPublicSchemaTest extends AbstractRLSWithSettingReferenceWithConstraintForCurrentTenantForeignKeyTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
