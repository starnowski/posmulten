package com.github.starnowski.posmulten.postgresql.core.functional.tests.combine;

public class CreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableInNonPublicSchemaTest extends AbstractCreateCurrentTenantCompositeForeignKeyConstraintForCommentsTableTest {
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
