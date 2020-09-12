package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory;

public class DefaultIsTenantIdentifierValidConstraintProducerParameters implements IIsTenantIdentifierValidConstraintProducerParameters{

    private final String constraintName;
    private final String tableName;
    private final String tableSchema;
    private final String tenantColumnName;
    private final IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory;

    public DefaultIsTenantIdentifierValidConstraintProducerParameters(String constraintName, String tableName, String tableSchema, String tenantColumnName, IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory) {
        this.constraintName = constraintName;
        this.tableName = tableName;
        this.tableSchema = tableSchema;
        this.tenantColumnName = tenantColumnName;
        this.iIsTenantValidFunctionInvocationFactory = iIsTenantValidFunctionInvocationFactory;
    }

    @Override
    public String getConstraintName() {
        return constraintName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getTableSchema() {
        return tableSchema;
    }

    @Override
    public String getTenantColumnName() {
        return tenantColumnName;
    }

    @Override
    public IIsTenantValidFunctionInvocationFactory getIIsTenantValidFunctionInvocationFactory() {
        return iIsTenantValidFunctionInvocationFactory;
    }

    public static class DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder
    {
        private String constraintName;
        private String tableName;
        private String tableSchema;
        private String tenantColumnName;
        private IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory;

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withTableSchema(String tableSchema) {
            this.tableSchema = tableSchema;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withTenantColumnName(String tenantColumnName) {
            this.tenantColumnName = tenantColumnName;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withIIsTenantValidFunctionInvocationFactory(IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory) {
            this.iIsTenantValidFunctionInvocationFactory = iIsTenantValidFunctionInvocationFactory;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParameters build()
        {
            return new DefaultIsTenantIdentifierValidConstraintProducerParameters(constraintName, tableName, tableSchema, tenantColumnName, iIsTenantValidFunctionInvocationFactory);
        }
    }
}
