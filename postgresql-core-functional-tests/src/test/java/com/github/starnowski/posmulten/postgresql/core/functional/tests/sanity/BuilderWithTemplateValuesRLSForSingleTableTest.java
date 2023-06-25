package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

import com.github.starnowski.posmulten.postgresql.core.context.decorator.BasicSharedSchemaContextDecorator;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder;

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
        DefaultDecoratorContext decoratorContext = DefaultDecoratorContext.builder()
                .withReplaceCharactersMap(MapBuilder.mapBuilder()
                        .put("{{template_schema_value}}", "non_public_schema")
                        .put("{{template_user_grantee}}", CORE_OWNER_USER)
                        .build())
                .build();
        sharedSchemaContext = new BasicSharedSchemaContextDecorator(sharedSchemaContext, decoratorContext);
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        sqlDefinitions.clear();
        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
    }

    @Override
    protected String getSchema() {
        return "non_public_schema";
    }
}
