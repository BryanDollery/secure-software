package com.dollery.services.vault.model;

public class AccessRule {
    private String role;
    private AccessMode access;

    AccessRule(String role, AccessMode access) {
        this.role = role;
        this.access = access;
    }

    String getRole() {
        return role;
    }

    public boolean isMode(AccessMode mode) {
        return this.access == mode;
    }

    AccessMode getAccess() {
        return access;
    }
}
