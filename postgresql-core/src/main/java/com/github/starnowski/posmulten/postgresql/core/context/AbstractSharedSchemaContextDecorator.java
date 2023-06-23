package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum;
import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public abstract class AbstractSharedSchemaContextDecorator extends DefaultDecorator<ISharedSchemaContext> implements ISharedSchemaContext {

    public AbstractSharedSchemaContextDecorator(ISharedSchemaContext sharedSchemaContext) {
        super(sharedSchemaContext);
    }

    @Override
    public List<SQLDefinition> getSqlDefinitions() {
        return value.getSqlDefinitions().stream().map(DefaultSQLDefinitionDecorator::new).collect(toList());
    }

    @Override
    public void addSQLDefinition(SQLDefinition sqlDefinition) {
        this.value.addSQLDefinition(sqlDefinition);
    }

    @Override
    public TenantHasAuthoritiesFunctionInvocationFactory getTenantHasAuthoritiesFunctionInvocationFactory() {
        return new DefaultTenantHasAuthoritiesFunctionInvocationFactoryDecorator(this.value.getTenantHasAuthoritiesFunctionInvocationFactory());
    }

    @Override
    public void setTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory factory) {
        this.value.setTenantHasAuthoritiesFunctionInvocationFactory(factory);
    }

    @Override
    public IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory() {
        return new DefaultGetCurrentTenantIdFunctionInvocationFactoryDecorator(this.value.getIGetCurrentTenantIdFunctionInvocationFactory());
    }

    @Override
    public void setIGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory factory) {
        this.value.setIGetCurrentTenantIdFunctionInvocationFactory(factory);
    }

    @Override
    public ISetCurrentTenantIdFunctionInvocationFactory getISetCurrentTenantIdFunctionInvocationFactory() {
        return new DefaultSetCurrentTenantIdFunctionInvocationFactoryDecorator(this.value.getISetCurrentTenantIdFunctionInvocationFactory());
    }

    @Override
    public void setISetCurrentTenantIdFunctionInvocationFactory(ISetCurrentTenantIdFunctionInvocationFactory factory) {
        this.value.setISetCurrentTenantIdFunctionInvocationFactory(factory);
    }

    @Override
    public ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory() {
        return new DefaultSetCurrentTenantIdFunctionPreparedStatementInvocationFactoryDecorator(this.value.getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory());
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
        return new DefaultIsTenantValidFunctionInvocationFactoryDecorator(this.value.getIIsTenantValidFunctionInvocationFactory());
    }

    @Override
    public void setIIsTenantValidFunctionInvocationFactory(IIsTenantValidFunctionInvocationFactory factory) {
        this.value.setIIsTenantValidFunctionInvocationFactory(factory);
    }

    @Override
    public String getCurrentTenantIdPropertyType() {
        return convert(this.value.getCurrentTenantIdPropertyType());
    }

    @Override
    public void setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        this.value.setCurrentTenantIdPropertyType(currentTenantIdPropertyType);
    }

    abstract protected String convert(String statement);

    class DefaultSQLDefinitionDecorator extends DefaultDecorator<SQLDefinition> implements SQLDefinition {

        DefaultSQLDefinitionDecorator(SQLDefinition value) {
            super(value);
        }

        @Override
        public String getCreateScript() {
            return convert(value.getCreateScript());
        }

        @Override
        public String getDropScript() {
            return convert(value.getDropScript());
        }

        @Override
        public List<String> getCheckingStatements() {
            return Optional.ofNullable(value.getCheckingStatements()).orElse(new ArrayList<>()).stream().map(AbstractSharedSchemaContextDecorator.this::convert).collect(toList());
        }
    }

    class DefaultSetCurrentTenantIdFunctionInvocationFactoryDecorator extends DefaultDecorator<ISetCurrentTenantIdFunctionInvocationFactory> implements ISetCurrentTenantIdFunctionInvocationFactory {

        DefaultSetCurrentTenantIdFunctionInvocationFactoryDecorator(ISetCurrentTenantIdFunctionInvocationFactory value) {
            super(value);
        }

        @Override
        public String generateStatementThatSetTenant(String tenantId) {
            return convert(this.value.generateStatementThatSetTenant(tenantId));
        }
    }

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
            return convert(value.returnTenantHasAuthoritiesFunctionInvocation(tenantIdValue, permissionCommandPolicy, rlsExpressionType, table, schema));
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
}
