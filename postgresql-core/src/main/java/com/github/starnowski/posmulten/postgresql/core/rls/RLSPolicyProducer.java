package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString;
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.USING;
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.WITH_CHECK;

public class RLSPolicyProducer {

    public SQLDefinition produce(RLSPolicyProducerParameters parameters)
    {
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters));
    }

    private String prepareDropScript(RLSPolicyProducerParameters parameters) {
        return null;
    }

    private String prepareCreateScript(RLSPolicyProducerParameters parameters) {
//        CREATE POLICY user_info_multi_tenant_policy ON user_info
//        FOR ALL
//        TO gdpr_user
//        USING (current_setting('poc.current_tenant') = 'public_use' OR tenant_id = current_setting('poc.current_tenant') )
//        WITH CHECK (current_setting('poc.current_tenant') = 'public_use' OR tenant_id = current_setting('poc.current_tenant') );
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
        sb.append(parameters.getGrantee());
        sb.append("\n");
        sb.append("USING ");
        sb.append("(");
        sb.append(prepareUsingRLSExpression(parameters));
        sb.append(")");
        sb.append("\n");
        sb.append("WITH CHECK ");
        sb.append("(");
        sb.append(prepareWithCheckRLSExpression(parameters));
        sb.append(")");
        sb.append(";");
        return sb.toString();
    }

    private String prepareWithCheckRLSExpression(RLSPolicyProducerParameters parameters) {
        return parameters.getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory().returnTenantHasAuthoritiesFunctionInvocation(prepareTenantIdColumnReference(parameters), parameters.getPermissionCommandPolicy(), WITH_CHECK, forString(parameters.getPolicyTable()), forString(parameters.getPolicySchema()));
    }

    private String prepareUsingRLSExpression(RLSPolicyProducerParameters parameters) {
        return parameters.getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory().returnTenantHasAuthoritiesFunctionInvocation(prepareTenantIdColumnReference(parameters), parameters.getPermissionCommandPolicy(), USING, forString(parameters.getPolicyTable()), forString(parameters.getPolicySchema()));
    }

    private FunctionArgumentValue prepareTenantIdColumnReference(RLSPolicyProducerParameters parameters) {
        return forReference(parameters.getTenantIdColumn() == null ? "tenant_id" : parameters.getTenantIdColumn());
    }
}
