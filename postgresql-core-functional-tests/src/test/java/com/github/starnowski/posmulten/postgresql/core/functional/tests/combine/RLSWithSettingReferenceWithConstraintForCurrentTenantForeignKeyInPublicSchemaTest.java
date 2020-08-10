package com.github.starnowski.posmulten.postgresql.core.functional.tests.combine;

public class RLSWithSettingReferenceWithConstraintForCurrentTenantForeignKeyInPublicSchemaTest extends AbstractRLSWithSettingReferenceWithConstraintForCurrentTenantForeignKeyTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
