package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum;
import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;

import java.util.List;
import java.util.Map;

public abstract class AbstractSharedSchemaContextDecorator implements ISharedSchemaContext {

    protected final ISharedSchemaContext sharedSchemaContext;

    public AbstractSharedSchemaContextDecorator(ISharedSchemaContext sharedSchemaContext) {
        this.sharedSchemaContext = sharedSchemaContext;
    }

    @Override
    public List<SQLDefinition> getSqlDefinitions() {
        return null;
    }

    @Override
    public void addSQLDefinition(SQLDefinition sqlDefinition) {
        this.sharedSchemaContext.addSQLDefinition(sqlDefinition);
    }

    @Override
    public TenantHasAuthoritiesFunctionInvocationFactory getTenantHasAuthoritiesFunctionInvocationFactory() {
        return new DefaultTenantHasAuthoritiesFunctionInvocationFactoryDecorator(this.sharedSchemaContext.getTenantHasAuthoritiesFunctionInvocationFactory());
    }

    @Override
    public void setTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory factory) {
        this.sharedSchemaContext.setTenantHasAuthoritiesFunctionInvocationFactory(factory);
    }

    @Override
    public IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory() {
        return new DefaultGetCurrentTenantIdFunctionInvocationFactoryDecorator(this.sharedSchemaContext.getIGetCurrentTenantIdFunctionInvocationFactory());
    }

    @Override
    public void setIGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory factory) {
        this.sharedSchemaContext.setIGetCurrentTenantIdFunctionInvocationFactory(factory);
    }

    @Override
    public ISetCurrentTenantIdFunctionInvocationFactory getISetCurrentTenantIdFunctionInvocationFactory() {
        return null;
    }

    @Override
    public void setISetCurrentTenantIdFunctionInvocationFactory(ISetCurrentTenantIdFunctionInvocationFactory factory) {
        this.sharedSchemaContext.setISetCurrentTenantIdFunctionInvocationFactory(factory);
    }

    @Override
    public ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory() {
        return new DefaultSetCurrentTenantIdFunctionPreparedStatementInvocationFactoryDecorator(this.sharedSchemaContext.getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory());
    }

    @Override
    public void setISetCurrentTenantIdFunctionPreparedStatementInvocationFactory(ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory factory) {
        this.setISetCurrentTenantIdFunctionPreparedStatementInvocationFactory(factory);
    }

    @Override
    public Map<TableKey, IsRecordBelongsToCurrentTenantFunctionInvocationFactory> getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap() {
        return null;
    }

    @Override
    public IIsTenantValidFunctionInvocationFactory getIIsTenantValidFunctionInvocationFactory() {
        return new DefaultIsTenantValidFunctionInvocationFactoryDecorator(this.sharedSchemaContext.getIIsTenantValidFunctionInvocationFactory());
    }

    @Override
    public void setIIsTenantValidFunctionInvocationFactory(IIsTenantValidFunctionInvocationFactory factory) {
        this.sharedSchemaContext.setIIsTenantValidFunctionInvocationFactory(factory);
    }

    @Override
    public String getCurrentTenantIdPropertyType() {
        return convert(this.sharedSchemaContext.getCurrentTenantIdPropertyType());
    }

    @Override
    public void setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        this.sharedSchemaContext.setCurrentTenantIdPropertyType(currentTenantIdPropertyType);
    }

    abstract protected String convert(String statement);

    class DefaultSetCurrentTenantIdFunctionPreparedStatementInvocationFactoryDecorator extends DefaultDecorator<ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory> implements ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory {

        DefaultSetCurrentTenantIdFunctionPreparedStatementInvocationFactoryDecorator(ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory value) {
            super(value);
        }

        @Override
        public String returnPreparedStatementThatSetCurrentTenant() {
            return convert(value.returnPreparedStatementThatSetCurrentTenant());
        }
    }

    class DefaultIsTenantValidFunctionInvocationFactoryDecorator extends DefaultDecorator<IIsTenantValidFunctionInvocationFactory> implements IIsTenantValidFunctionInvocationFactory {

        DefaultIsTenantValidFunctionInvocationFactoryDecorator(IIsTenantValidFunctionInvocationFactory value) {
            super(value);
        }

        @Override
        public String returnIsTenantValidFunctionInvocation(FunctionArgumentValue argumentValue) {
            return convert(value.returnIsTenantValidFunctionInvocation(argumentValue));
        }
    }

    class DefaultTenantHasAuthoritiesFunctionInvocationFactoryDecorator extends DefaultDecorator<TenantHasAuthoritiesFunctionInvocationFactory> implements TenantHasAuthoritiesFunctionInvocationFactory {
        DefaultTenantHasAuthoritiesFunctionInvocationFactoryDecorator(TenantHasAuthoritiesFunctionInvocationFactory value) {
            super(value);
        }

        @Override
        public String returnTenantHasAuthoritiesFunctionInvocation(FunctionArgumentValue tenantIdValue, PermissionCommandPolicyEnum permissionCommandPolicy, RLSExpressionTypeEnum rlsExpressionType, FunctionArgumentValue table, FunctionArgumentValue schema) {
            return convert(value.returnTenantHasAuthoritiesFunctionInvocation(tenantIdValue, permissionCommandPolicy, rlsExpressionType, tenantIdValue, schema));
        }
    }

    class DefaultGetCurrentTenantIdFunctionInvocationFactoryDecorator extends DefaultDecorator<IGetCurrentTenantIdFunctionInvocationFactory> implements IGetCurrentTenantIdFunctionInvocationFactory {

        DefaultGetCurrentTenantIdFunctionInvocationFactoryDecorator(IGetCurrentTenantIdFunctionInvocationFactory value) {
            super(value);
        }

        @Override
        public String returnGetCurrentTenantIdFunctionInvocation() {
            return convert(value.returnGetCurrentTenantIdFunctionInvocation());
        }
    }

    class DefaultDecorator<T> implements IDecorator<T> {

        protected final T value;

        DefaultDecorator(T value) {
            this.value = value;
        }

        @Override
        public T unwrap() {
            return value instanceof IDecorator ? ((IDecorator<T>) value).unwrap() : value;
        }
    }
}
