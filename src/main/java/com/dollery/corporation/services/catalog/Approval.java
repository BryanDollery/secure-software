package com.dollery.corporation.services.catalog;

public class Approval implements Comparable<Approval> {
    private Member member;

    public Approval(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public int compareTo(Approval approval) {
        return member.getName().compareTo(approval.member.getName());
    }
}
