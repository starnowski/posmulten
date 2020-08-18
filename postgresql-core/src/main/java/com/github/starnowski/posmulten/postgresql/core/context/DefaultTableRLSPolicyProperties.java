package com.github.starnowski.posmulten.postgresql.core.context;

public class DefaultTableRLSPolicyProperties implements AbstractTableRLSPolicyProperties{

    private final String policyName;

    public DefaultTableRLSPolicyProperties(String policyName) {
        this.policyName = policyName;
    }

    @Override
    public String getPolicyName() {
        return policyName;
    }
}
