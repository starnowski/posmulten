package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

public class RLSWithSettingReferenceWithoutConstraintForCurrentTenantForeignKeyInNonPublicSchemaTest extends AbstractRLSWithSettingReferenceWithoutConstraintForCurrentTenantForeignKeyTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}