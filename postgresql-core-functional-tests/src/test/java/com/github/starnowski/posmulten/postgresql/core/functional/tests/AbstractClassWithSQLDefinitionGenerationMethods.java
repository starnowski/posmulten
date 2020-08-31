package com.github.starnowski.posmulten.postgresql.core.functional.tests;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.rls.function.IIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType;

public abstract class AbstractClassWithSQLDefinitionGenerationMethods extends TestNGSpringContextWithoutGenericTransactionalSupportTests {

    protected static final String POSTS_USERS_FK_CONSTRAINT_NAME = "posts_users_fk_cu";
    protected static final String COMMENTS_USERS_FK_CONSTRAINT_NAME = "comments_users_fk_cu";
    protected static final String COMMENTS_POSTS_FK_CONSTRAINT_NAME = "comments_posts_fk_cu";
    protected static final String COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME = "comments_parent_comments_fk_cu";
    protected static final String NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME = "notifications_users_fk_cu";
    protected static final String USERS_GROUPS_USERS_FK_CONSTRAINT_NAME = "users_groups_users_fk_cu";
    protected static final String USERS_GROUPS_GROUPS_FK_CONSTRAINT_NAME = "users_groups_groups_fk_cu";

    abstract protected String getSchema();

    protected SQLDefinition getSqlDefinitionOfConstraintForUsersForeignKeyInPostsTable(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isUsersRecordBelongsToCurrentTenantFunctionDefinition) {
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("user_id"));
        return getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(isUsersRecordBelongsToCurrentTenantFunctionDefinition, POSTS_USERS_FK_CONSTRAINT_NAME, POSTS_TABLE_NAME, primaryColumnsValuesMap);
    }

    protected SQLDefinition getSqlDefinitionOfConstraintForUsersForeignKeyInCommentsTable(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isUsersRecordBelongsToCurrentTenantFunctionDefinition) {
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("user_id"));
        return getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(isUsersRecordBelongsToCurrentTenantFunctionDefinition, COMMENTS_USERS_FK_CONSTRAINT_NAME, COMMENTS_TABLE_NAME, primaryColumnsValuesMap);
    }

    protected SQLDefinition getSqlDefinitionOfConstraintForUsersForeignKeyInNotificationsTable(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isNotificationsRecordBelongsToCurrentTenantFunctionDefinition) {
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("user_id"));
        return getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(isNotificationsRecordBelongsToCurrentTenantFunctionDefinition, NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME, NOTIFICATIONS_TABLE_NAME, primaryColumnsValuesMap);
    }

    protected SQLDefinition getSqlDefinitionOfConstraintForPostsForeignKeyInCommentsTable(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isPostsRecordBelongsToCurrentTenantFunctionDefinition) {
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("post_id"));
        return getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(isPostsRecordBelongsToCurrentTenantFunctionDefinition, COMMENTS_POSTS_FK_CONSTRAINT_NAME, COMMENTS_TABLE_NAME, primaryColumnsValuesMap);
    }

    protected SQLDefinition getSqlDefinitionOfConstraintForParentCommentForeignKeyInCommentsTable(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isPostsRecordBelongsToCurrentTenantFunctionDefinition) {
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("parent_comment_id"));
        primaryColumnsValuesMap.put("user_id", forReference("parent_comment_user_id"));
        return getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(isPostsRecordBelongsToCurrentTenantFunctionDefinition, COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME, COMMENTS_TABLE_NAME, primaryColumnsValuesMap);
    }

    protected SQLDefinition getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isUsersRecordBelongsToCurrentTenantFunctionDefinition, String constraintName, String tableName, Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer = new IsRecordBelongsToCurrentTenantConstraintProducer();
        IsRecordBelongsToCurrentTenantConstraintProducerParameters isRecordBelongsToCurrentTenantConstraintProducerParameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(constraintName)
                .withTableName(tableName)
                .withTableSchema(getSchema())
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isUsersRecordBelongsToCurrentTenantFunctionDefinition)
                .withPrimaryColumnsValuesMap(primaryColumnsValuesMap).build();
        return isRecordBelongsToCurrentTenantConstraintProducer.produce(isRecordBelongsToCurrentTenantConstraintProducerParameters);
    }

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsUsersRecordBelongsToCurrentTenantFunctionDefinition(IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionDefinition) {
        IIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(getSchema())
                .withFunctionName("is_user_belongs_to_current_tenant")
                .withRecordTableName("users")
                .withRecordSchemaName(getSchema())
                .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionDefinition)
                .withTenantColumn("tenant_id")
                .withKeyColumnsPairsList(ImmutableList.of(pairOfColumnWithType("id", "bigint"))).build();
        IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();
        return isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
    }

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsPostsRecordBelongsToCurrentTenantFunctionDefinition(IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionDefinition) {
        IIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(getSchema())
                .withFunctionName("is_post_belongs_to_current_tenant")
                .withRecordTableName("posts")
                .withRecordSchemaName(getSchema())
                .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionDefinition)
                .withTenantColumn("tenant_id")
                .withKeyColumnsPairsList(ImmutableList.of(pairOfColumnWithType("id", "bigint"))).build();
        IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();
        return isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
    }

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsCommentsRecordBelongsToCurrentTenantFunctionDefinition(IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionDefinition) {
        IIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(getSchema())
                .withFunctionName("is_comment_belongs_to_current_tenant")
                .withRecordTableName("comments")
                .withRecordSchemaName(getSchema())
                .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionDefinition)
                .withTenantColumn("tenant")
                .withKeyColumnsPairsList(ImmutableList.of(pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint"))).build();
        IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();
        return isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
    }

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsNotificationsRecordBelongsToCurrentTenantFunctionDefinition(IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionDefinition) {
        IIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(getSchema())
                .withFunctionName("is_notification_belongs_to_current_tenant")
                .withRecordTableName(NOTIFICATIONS_TABLE_NAME)
                .withRecordSchemaName(getSchema())
                .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionDefinition)
                .withTenantColumn("tenant")
                .withKeyColumnsPairsList(ImmutableList.of(pairOfColumnWithType("uuid", "uuid"))).build();
        IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();
        return isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
    }

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsGroupsRecordBelongsToCurrentTenantFunctionDefinition(IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionDefinition) {
        IIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(getSchema())
                .withFunctionName("is_group_belongs_to_current_tenant")
                .withRecordTableName(GROUPS_TABLE_NAME)
                .withRecordSchemaName(getSchema())
                .withiGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionDefinition)
                .withTenantColumn("tenant_id")
                .withKeyColumnsPairsList(ImmutableList.of(pairOfColumnWithType("uuid", "uuid"))).build();
        IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();
        return isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
    }
}
