package com.github.starnowski.posmulten.postgresql.core.context

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME
import static java.util.Arrays.asList

class DefaultSharedSchemaContextBuilderSmokeTest extends Specification {

    @Unroll
    def "run smoke test for builder with schema #schema" ()
    {
        given:
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
            def tested = (new DefaultSharedSchemaContextBuilder(schema)).setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME)
                .setForceRowLevelSecurityForTableOwner(true)
                .setGrantee(CORE_OWNER_USER)
                .createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME)
                .createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy")
                .createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy")
                .createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy")
                .createRLSPolicyForTable(GROUPS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), "tenant_id", "groups_table_rls_policy")
                .createRLSPolicyForTable(USERS_GROUPS_TABLE_NAME, new HashMap<>(), "tenant_id", "users_groups_table_rls_policy")
                .createRLSPolicyForTable(COMMENTS_TABLE_NAME, mapBuilder().put("id", "int").put("user_id", "bigint").build(), CUSTOM_TENANT_COLUMN_NAME, "comments_table_rls_policy")
                .createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), POSTS_USERS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), COMMENTS_USERS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, POSTS_TABLE_NAME, mapBuilder().put("post_id", "id").build(), COMMENTS_POSTS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, COMMENTS_TABLE_NAME, mapBuilder().put("parent_comment_id", "id").put("parent_comment_user_id", "user_id").build(), COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME)
                .setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant")
                .setNameForFunctionThatChecksIfRecordExistsInTable(POSTS_TABLE_NAME, "is_post_belongs_to_current_tenant")
                .setNameForFunctionThatChecksIfRecordExistsInTable(COMMENTS_TABLE_NAME, "is_comment_belongs_to_current_tenant")
                .setNameForFunctionThatChecksIfRecordExistsInTable(GROUPS_TABLE_NAME, "is_group_belongs_to_current_tenant")

        when:
            def sharedSchemaContext = tested.build()

        then:
            !sharedSchemaContext.getSqlDefinitions().isEmpty()

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "run smoke test for builder with schema #schema and with custom function names" ()
    {
        given:
            String CORE_OWNER_USER = "postgresql-core-owner"
            String CUSTOM_TENANT_COLUMN_NAME = "tenant"
            String USERS_TABLE_NAME = "users"
            String POSTS_TABLE_NAME = "posts"
            String NOTIFICATIONS_TABLE_NAME = "notifications"
            String POSTS_USERS_FK_CONSTRAINT_NAME = "posts_users_fk_cu"
            String NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME = "notifications_users_fk_cu"
            def tested = (new DefaultSharedSchemaContextBuilder(schema)).setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME)
                    .setForceRowLevelSecurityForTableOwner(true)
                    .setGrantee(CORE_OWNER_USER)
                    .createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME)
                    .createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy")
                    .createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy")
                    .createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy")
                    .createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), POSTS_USERS_FK_CONSTRAINT_NAME)
                    .createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME)
                    .setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant")
                    .setNameForFunctionThatChecksIfRecordExistsInTable(POSTS_TABLE_NAME, "is_post_belongs_to_current_tenant")
                    .createValidTenantValueConstraint(asList("DUMMMY_TENANT", "XXX-INVAlid_tenant"), "is_tenant_valid", "tenant_should_be_valid")
                    .registerCustomValidTenantValueConstraintNameForTable("posts", "posts_tenant_is_valid")
                    .setDefaultTenantIdColumn("tenant_uuid")
                    .setTenantHasAuthoritiesFunctionName("tenant_has_aut_fun")
                    .setEqualsCurrentTenantIdentifierFunctionName("eq_to_current_tenant")
                    .setSetCurrentTenantIdFunctionName("setting_the_tenant_value_is")
                    .setGetCurrentTenantIdFunctionName("getting_current_tenant_identifier")
                    .setCurrentTenantIdPropertyType("VARCHAR(128)")

        when:
            def sharedSchemaContext = tested.build()

        then:
            !sharedSchemaContext.getSqlDefinitions().isEmpty()

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "run smoke test for builder with schema #schema when no optional options are passed to builder" ()
    {
        given:
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
        def tested = (new DefaultSharedSchemaContextBuilder(schema)).setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME)
                .setForceRowLevelSecurityForTableOwner(true)
                .setGrantee(CORE_OWNER_USER)
                .createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME)
                .createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy")
                .createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), null, "users_table_rls_policy")
                .createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), null, "posts_table_rls_policy")
                .createRLSPolicyForTable(GROUPS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), null, "groups_table_rls_policy")
                .createRLSPolicyForTable(USERS_GROUPS_TABLE_NAME, new HashMap<>(), null, "users_groups_table_rls_policy")
                .createRLSPolicyForTable(COMMENTS_TABLE_NAME, mapBuilder().put("id", "int").put("user_id", "bigint").build(), CUSTOM_TENANT_COLUMN_NAME, "comments_table_rls_policy")
                .createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), POSTS_USERS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), COMMENTS_USERS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, POSTS_TABLE_NAME, mapBuilder().put("post_id", "id").build(), COMMENTS_POSTS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, COMMENTS_TABLE_NAME, mapBuilder().put("parent_comment_id", "id").put("parent_comment_user_id", "user_id").build(), COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME)
                .createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME)
                .setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant")
                .setNameForFunctionThatChecksIfRecordExistsInTable(POSTS_TABLE_NAME, "is_post_belongs_to_current_tenant")
                .setNameForFunctionThatChecksIfRecordExistsInTable(COMMENTS_TABLE_NAME, "is_comment_belongs_to_current_tenant")
                .setNameForFunctionThatChecksIfRecordExistsInTable(GROUPS_TABLE_NAME, "is_group_belongs_to_current_tenant")
                .createValidTenantValueConstraint(asList("DUMMMY_TENANT", "XXX-INVAlid_tenant"), null, null)

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
