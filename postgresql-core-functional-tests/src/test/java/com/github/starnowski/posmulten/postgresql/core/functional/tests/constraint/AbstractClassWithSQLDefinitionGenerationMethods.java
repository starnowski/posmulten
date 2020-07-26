package com.github.starnowski.posmulten.postgresql.core.functional.tests.constraint;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.TestNGSpringContextWithoutGenericTransactionalSupportTests;
import com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType;

public abstract class AbstractClassWithSQLDefinitionGenerationMethods extends TestNGSpringContextWithoutGenericTransactionalSupportTests {

    protected static final String POSTS_USERS_FK_CONSTRAINT_NAME = "posts_user_info_fk_cu";
    protected static final String COMMENTS_USERS_FK_CONSTRAINT_NAME = "comments_user_info_fk_cu";

    abstract protected String getSchema();

    protected SQLDefinition getSqlDefinitionOfConstraintForUsersForeignKeyInPostsTable(IsRecordBelongsToCurrentTenantFunctionDefinition isUsersRecordBelongsToCurrentTenantFunctionDefinition) {
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("user_id"));
        return getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(isUsersRecordBelongsToCurrentTenantFunctionDefinition, POSTS_USERS_FK_CONSTRAINT_NAME, "posts", primaryColumnsValuesMap);
    }

    protected SQLDefinition getSqlDefinitionOfConstraintForUsersForeignKeyInCommentsTable(IsRecordBelongsToCurrentTenantFunctionDefinition isUsersRecordBelongsToCurrentTenantFunctionDefinition) {
        Map<String, FunctionArgumentValue> primaryColumnsValuesMap = new HashMap<>();
        primaryColumnsValuesMap.put("id", forReference("user_id"));
        return getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(isUsersRecordBelongsToCurrentTenantFunctionDefinition, COMMENTS_USERS_FK_CONSTRAINT_NAME, "comments", primaryColumnsValuesMap);
    }

    protected SQLDefinition getSqlDefinitionOfConstraintForMultiTenantTableForeignKey(IsRecordBelongsToCurrentTenantFunctionDefinition isUsersRecordBelongsToCurrentTenantFunctionDefinition, String constraintName, String tableName, Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        IsRecordBelongsToCurrentTenantConstraintProducer isRecordBelongsToCurrentTenantConstraintProducer = new IsRecordBelongsToCurrentTenantConstraintProducer();
        IsRecordBelongsToCurrentTenantConstraintProducerParameters isRecordBelongsToCurrentTenantConstraintProducerParameters = DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters.builder()
                .withConstraintName(constraintName)
                .withTableName(tableName)
                .withTableSchema(getSchema())
                .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isUsersRecordBelongsToCurrentTenantFunctionDefinition)
                .withPrimaryColumnsValuesMap(primaryColumnsValuesMap).build();
        return isRecordBelongsToCurrentTenantConstraintProducer.produce(isRecordBelongsToCurrentTenantConstraintProducerParameters);
    }

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsUsersRecordBelongsToCurrentTenantFunctionDefinition(GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition) {
        AbstractIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
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

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsPostsRecordBelongsToCurrentTenantFunctionDefinition(GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition) {
        AbstractIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
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

    protected IsRecordBelongsToCurrentTenantFunctionDefinition getIsCommentsRecordBelongsToCurrentTenantFunctionDefinition(GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition) {
        AbstractIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
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
}
