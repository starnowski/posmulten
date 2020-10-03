package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.DefaultTestNGTest;
import org.testng.annotations.Test;

import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;

public abstract class AbstractCreateTenantIdentifierValidConstraintForRLSTablesTest extends DefaultTestNGTest {

    protected static final String CUSTOM_TENANT_COLUMN_NAME = "tenant";

    abstract protected String getSchema();

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware and also creates column for tenant id")
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        ISharedSchemaContext result = (new DefaultSharedSchemaContextBuilder(getSchema())).setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME)
                .setForceRowLevelSecurityForTableOwner(true)
                .setGrantee(CORE_OWNER_USER)
                .createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME)
                .createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy")
                .createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy")
                .createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy")
                .createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), "post_users_tenant_fk")
                .createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), "notification_users_tenant_fk")
                .setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant")
                .build();
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType)
    {
        return mapBuilder().put(columnName, columnType).build();
    }
}
