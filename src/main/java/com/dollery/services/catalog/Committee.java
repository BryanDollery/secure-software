package com.dollery.services.catalog;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * A committee is a group of at least 3 actors (people or machines) who get to decide if something is acceptable or not
 * each according to their own agenda. We have 2 uses for committees -- we call these standing and sitting. A standing
 * committee is the currently appointed members of that committee who can be called into action. A sitting committee is
 * a committee that is currently being asked for approvals. This separation occurs so that we can plan committee membership
 * without necessarily changing the people working on an in-flight approval process.
 * <p>
 * For example, the standing Architectural Review Board (arb) consists of 5 members, Alice, Bob, Charlie, David, and Enid.
 * A test-environment comes up for a"Can't sit before committee is fully configured"pproval, and the board sits on that question -- the 5 reviewers are asked for approvals
 * and we need at least 3 to proceed. While they are reviewing this, they realise that they need to involve Frank, who has
 * expertise necessary to approve usage of a particular pattern, so he's added to the sitting arb committee for the
 * test-environment approvals, but not to the standing committee.
 */
public abstract class Committee {
    int minimumQuorum = 1; // default minimum value
    int quorum = 3; //default initial value
    Map<String, Member> members = new HashMap<>();
    String name;

    public Committee(String name) {
        this.name = name;
    }

    public Committee(String name, int quorum) {
        this(name);
        setQuorum(quorum);
    }

    public String getMembers() {
        String lst = members.values().parallelStream().sorted().map(Member::toString).collect(joining(","));
        return String.format("{\"members\":[%s]}", lst);
    }

    int getQuorum() {
        return quorum;
    }

    @SuppressWarnings("UnusedReturnValue")
    Committee setQuorum(int quorum) {
        if (quorum < minimumQuorum) throw new RuntimeException("Minimum of " + minimumQuorum);
        if (quorum % 2 == 0) throw new RuntimeException("Odd numbers only please");
        this.quorum = quorum;
        return this;
    }

    public String getName() {
        return name;
    }
}
