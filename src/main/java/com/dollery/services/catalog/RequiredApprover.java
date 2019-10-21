package com.dollery.services.catalog;

public class RequiredApprover {
    private String role;
    private int quorum = 3;

    public RequiredApprover(String role) {
        this.role = role;
    }

    public RequiredApprover(String role, int quorum) {
        this(role);
        this.quorum = quorum;
    }

    public RequiredApprover setQuorum(int quorum) {
        if (quorum < 3) throw new RuntimeException("Minimum of 3");
        if (quorum % 2 == 0) throw new RuntimeException("Odd numbers only please");
        this.quorum = quorum;
        return this;
    }
}
