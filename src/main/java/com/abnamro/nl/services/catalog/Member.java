package com.abnamro.nl.services.catalog;

public class Member implements Comparable<Member> {
    public String name;

    public Member(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Member member) {
        return name.compareTo(member.name);
    }

    public String getName() {
        return name;
    }
}
