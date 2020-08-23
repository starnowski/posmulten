package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

public class RLSPolicyAndForeignKeyConstraintInManyToManyTableInPublicSchemaTest extends AbstractRLSPolicyAndForeignKeyConstraintInManyToManyTableTest{
    @Override
    protected String getSchema() {
        return null;
    }
}
