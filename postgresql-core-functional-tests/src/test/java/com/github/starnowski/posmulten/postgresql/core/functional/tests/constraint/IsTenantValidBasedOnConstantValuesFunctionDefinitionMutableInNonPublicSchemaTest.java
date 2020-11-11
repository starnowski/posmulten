package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

public class IsTenantValidBasedOnConstantValuesFunctionDefinitionMutableInNonPublicSchemaTest extends AbstractIsTenantValidBasedOnConstantValuesFunctionDefinitionMutableTest {
    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
