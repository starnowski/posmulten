package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

public class CreateCurrentTenantForeignKeyConstraintForPostsTableFromPublicSchemaTest extends AbstractCreateCurrentTenantForeignKeyConstraintForPostsTableTest{

    @Override
    protected String getSchema() {
        return null;
    }
}
