package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

public class CreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableFromNonPublicSchemaTest extends AbstractCreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
