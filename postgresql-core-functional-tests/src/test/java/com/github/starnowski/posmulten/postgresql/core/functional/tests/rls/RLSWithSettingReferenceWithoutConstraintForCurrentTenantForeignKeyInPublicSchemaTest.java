package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

public class RLSWithSettingReferenceWithoutConstraintForCurrentTenantForeignKeyInPublicSchemaTest extends AbstractRLSWithSettingReferenceWithoutConstraintForCurrentTenantForeignKeyTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
