package com.github.starnowski.posmulten.postgresql.core.rls;

public interface IRLSPolicyProducerParameters {

    String getPolicyName();



    RLSExpressionTypeEnum getRlsExpressionType();

    PermissionCommandPolicyEnum getPermissionCommandPolicy();
}
