package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

public class CreateCurrentTenantForeignKeyConstraintForPostsTableFromNonPublicSchemaTest extends AbstractCreateCurrentTenantForeignKeyConstraintForPostsTableTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
