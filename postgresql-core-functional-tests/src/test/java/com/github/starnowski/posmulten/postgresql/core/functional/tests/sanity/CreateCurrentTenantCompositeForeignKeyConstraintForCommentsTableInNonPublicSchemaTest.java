package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class CreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableInNonPublicSchemaTest extends AbstractCreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableTest {
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
