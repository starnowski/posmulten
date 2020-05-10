package com.github.starnowski.posmulten.postgresql.core.rls;

public enum PermissionCommandPolicyEnum implements PermissionCommandPolicySupplier{
    ALL,
    SELECT,
    INSERT,
    UPDATE,
    DELETE;

    @Override
    public String getPermissionCommandPolicyString() {
        return name();
    }
}
