package com.dollery.services.vault.model;

public enum AccessMode {
    r("r"), w("w"), rw("rw");

    private String access;

    AccessMode(String access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return access;
    }
}
