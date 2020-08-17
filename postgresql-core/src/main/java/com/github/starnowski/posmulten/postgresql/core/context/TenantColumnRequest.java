package com.github.starnowski.posmulten.postgresql.core.context;

public class TenantColumnRequest implements AbstractTenantColumnRequest{

    private final String name;

    public String getName() {
        return name;
    }

    public TenantColumnRequest(String name) {
        this.name = name;
    }
}
