package com.github.starnowski.posmulten.postgresql.core.context

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.MapBuilder.mapBuilder
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME

class DefaultSharedSchemaContextBuilderSmokeTest extends Specification {

    @Unroll
    def "run smoke test for builder with schema #schema" ()
    {
        given:
            def tested = new DefaultSharedSchemaContextBuilder(schema)
            String CORE_OWNER_USER = "postgresql-core-owner"
            String CUSTOM_TENANT_COLUMN_NAME = "tenant"
            String USERS_TABLE_NAME = "users"
            String POSTS_TABLE_NAME = "posts"
            String GROUPS_TABLE_NAME = "groups"
            String USERS_GROUPS_TABLE_NAME = "users_groups"
            String COMMENTS_TABLE_NAME = "comments"
            String NOTIFICATIONS_TABLE_NAME = "notifications"
            String POSTS_USERS_FK_CONSTRAINT_NAME = "posts_users_fk_cu"
            String COMMENTS_USERS_FK_CONSTRAINT_NAME = "comments_users_fk_cu"
            String COMMENTS_POSTS_FK_CONSTRAINT_NAME = "comments_posts_fk_cu"
            String COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME = "comments_parent_comments_fk_cu"
            String NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME = "notifications_users_fk_cu"
            tested.setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME)
            tested.setForceRowLevelSecurityForTableOwner(true)
            tested.setGrantee(CORE_OWNER_USER)
            tested.createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME)
            tested.createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy")
            tested.createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy")
            tested.createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy")
            tested.createRLSPolicyForTable(GROUPS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), "tenant_id", "groups_table_rls_policy")
            tested.createRLSPolicyForTable(USERS_GROUPS_TABLE_NAME, new HashMap<>(), "tenant_id", "users_groups_table_rls_policy")
            tested.createRLSPolicyForTable(COMMENTS_TABLE_NAME, mapBuilder().put("id", "int").put("user_id", "bigint").build(), CUSTOM_TENANT_COLUMN_NAME, "comments_table_rls_policy")
            tested.createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), POSTS_USERS_FK_CONSTRAINT_NAME)
            tested.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), COMMENTS_USERS_FK_CONSTRAINT_NAME)
            tested.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, POSTS_TABLE_NAME, mapBuilder().put("post_id", "id").build(), COMMENTS_POSTS_FK_CONSTRAINT_NAME)
            tested.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, COMMENTS_TABLE_NAME, mapBuilder().put("parent_comment_id", "id").put("parent_comment_user_id", "user_id").build(), COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME)
            tested.createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME)

            tested.setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant")
            tested.setNameForFunctionThatChecksIfRecordExistsInTable(POSTS_TABLE_NAME, "is_post_belongs_to_current_tenant")
            tested.setNameForFunctionThatChecksIfRecordExistsInTable(COMMENTS_TABLE_NAME, "is_comment_belongs_to_current_tenant")
            tested.setNameForFunctionThatChecksIfRecordExistsInTable(GROUPS_TABLE_NAME, "is_group_belongs_to_current_tenant")

        when:
            def sharedSchemaContext = tested.build()

        then:
            !sharedSchemaContext.getSqlDefinitions().isEmpty()

        where:
            schema << [null, "public", "some_schema"]
    }

    def prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType)
    {
        mapBuilder().put(columnName, columnType).build();
    }
}
