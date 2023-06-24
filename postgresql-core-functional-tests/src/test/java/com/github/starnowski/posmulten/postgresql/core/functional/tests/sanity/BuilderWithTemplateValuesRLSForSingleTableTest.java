package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

import com.github.starnowski.posmulten.postgresql.core.context.BasicSharedSchemaContextDecorator;
import com.github.starnowski.posmulten.postgresql.core.context.BasicSharedSchemaContextDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder;

import java.util.Map;

public class BuilderWithTemplateValuesRLSForSingleTableTest extends AbstractRLSForSingleTableTest {
    @Override
    protected String getSchemaForSharedSchemaContextBuilderInitialization() {
        return "{{template_schema_value}}";
    }

    @Override
    protected String getGranteeForSharedSchemaContextBuilderInitialization() {
        return "{{template_user_grantee}}";
    }

    @Override
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        super.createSQLDefinitions();
        sharedSchemaContext = new BasicSharedSchemaContextDecorator(sharedSchemaContext, new BasicSharedSchemaContextDecoratorContext() {
            @Override
            public Map<String, String> getVariableValueMap() {
                return MapBuilder.mapBuilder().put("{{template_schema_value}}", "non_public_schema").put("{{template_user_grantee}}", CORE_OWNER_USER).build();
            }
        });
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        sqlDefinitions.clear();
        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
    }

    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
