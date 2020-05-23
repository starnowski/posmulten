package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString;
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.INSERT;
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.USING;
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.WITH_CHECK;
import static java.lang.String.format;

public class RLSPolicyProducer {

    public SQLDefinition produce(RLSPolicyProducerParameters parameters)
    {
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters));
    }

    private String prepareDropScript(RLSPolicyProducerParameters parameters) {
        String tableReference = prepareTableReference(parameters);
        return format("DROP POLICY IF EXISTS %1$s ON %2$s", parameters.getPolicyName(), tableReference);
    }

    private String prepareTableReference(RLSPolicyProducerParameters parameters) {
        return parameters.getPolicySchema() == null ? parameters.getPolicyTable() : parameters.getPolicySchema() + "." + parameters.getPolicyTable();
    }

    private String prepareCreateScript(RLSPolicyProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE POLICY ");
        sb.append(parameters.getPolicyName());
        sb.append(" ON ");
        if (parameters.getPolicySchema() != null)
        {
            sb.append(parameters.getPolicySchema());
            sb.append(".");
        }
        sb.append(parameters.getPolicyTable());
        sb.append("\n");
        sb.append("FOR ");
        sb.append(parameters.getPermissionCommandPolicy());
        sb.append("\n");
        sb.append("TO ");
        sb.append("\"");
        sb.append(parameters.getGrantee());
        sb.append("\"");
        if (isUsingExpressionShouldBeApplied(parameters)) {
            sb.append("\n");
            sb.append("USING ");
            sb.append("(");
            sb.append(prepareUsingRLSExpression(parameters));
            sb.append(")");
        }
        sb.append("\n");
        sb.append("WITH CHECK ");
        sb.append("(");
        sb.append(prepareWithCheckRLSExpression(parameters));
        sb.append(")");
        sb.append(";");
        return sb.toString();
    }

    private boolean isUsingExpressionShouldBeApplied(RLSPolicyProducerParameters parameters) {
        return !INSERT.equals(parameters.getPermissionCommandPolicy());
    }

    private String prepareWithCheckRLSExpression(RLSPolicyProducerParameters parameters) {
        return parameters.getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory().returnTenantHasAuthoritiesFunctionInvocation(prepareTenantIdColumnReference(parameters), parameters.getPermissionCommandPolicy(), WITH_CHECK, forString(parameters.getPolicyTable()), preparePolicySchemaFunctionArgument(parameters.getPolicySchema()));
    }

    private FunctionArgumentValue preparePolicySchemaFunctionArgument(String schema) {
        return schema == null ? forString("public") : forString(schema);
    }

    private String prepareUsingRLSExpression(RLSPolicyProducerParameters parameters) {
        return parameters.getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory().returnTenantHasAuthoritiesFunctionInvocation(prepareTenantIdColumnReference(parameters), parameters.getPermissionCommandPolicy(), USING, forString(parameters.getPolicyTable()), preparePolicySchemaFunctionArgument(parameters.getPolicySchema()));
    }

    private FunctionArgumentValue prepareTenantIdColumnReference(RLSPolicyProducerParameters parameters) {
        return forReference(parameters.getTenantIdColumn() == null ? "tenant_id" : parameters.getTenantIdColumn());
    }
}
