package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSPolicyAndForeignKeyConstraintInManyToManyTableInNonPublicSchemaTest extends AbstractRLSPolicyAndForeignKeyConstraintInManyToManyTableTest{
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
