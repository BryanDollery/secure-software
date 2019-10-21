package com.dollery.services.vault.model;

public class AccessRule {
    private String role;
    private AccessMode access;

    public AccessRule(String role, AccessMode access) {
        this.role = role;
        this.access = access;
    }

    public String getRole() {
        return role;
    }

    public boolean isMode(AccessMode mode) {
        return this.access == mode;
    }

    public AccessMode getAccess() {
        return access;
    }
}
