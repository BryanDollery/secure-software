package com.dollery.services.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static com.dollery.services.Colors.COMMITTEE;
import static com.dollery.services.Colors.DATA;
import static com.dollery.services.Colors.DATA2;
import static com.dollery.services.Colors.MEMBER;
import static com.dollery.services.Colors.RED;
import static com.dollery.services.Colors.STATUS;
import static com.dollery.services.catalog.Sem.patch;
import static com.dollery.services.catalog.SittingCommittee.Status.approved;
import static java.util.stream.Collectors.joining;

/**
 * A committee is a group of at least 3 actors (people or machines) who get to decide if something is acceptable or not
 * each according to their own agenda. We have 2 uses for committees -- we call these standing and sitting. A standing
 * committee is the currently appointed members of that committee who can be called into action. A sitting committee is
 * a committee that is currently being asked for approvals. This separation occurs so that we can plan committee membership
 * without necessarily changing the people working on an in-flight approval process.
 * <p>
 * For example, the standing Architectural Review Board (arb) consists of 5 members, Alice, Bob, Charlie, David, and Enid.
 * A test-environment comes up for a"Can't sit before committee is fully configured approval, and the board sits on that
 * question -- the 5 reviewers are asked for approvals and we need at least 3 to proceed. While they are reviewing this,
 * they realise that they need to involve Frank, who has expertise necessary to approve usage of a particular pattern,
 * so he's added to the sitting arb committee for the test-environment approvals, but not to the standing committee.
 */
public class SittingCommittee extends Committee {
    private static final Logger log = LoggerFactory.getLogger(SittingCommittee.class);

    private Status status = Status.sitting;
    private Set<Approval> approvals = new HashSet<>();
    private Sem resitLevel = patch;

    public SittingCommittee(StandingCommittee standingCommittee, Sem resitEvery) {
        super(standingCommittee.getName());

        if (standingCommittee.status == StandingCommittee.Status.unready) {
            log.debug("Can't sit before there are at least quorum + 2 members. Currently there are {}/{}", members.size(), quorum);
            throw new RuntimeException("Can't sit before there are at least quorum + 2 members. Currently there are " + members.size() + "/" + quorum);
        }

        standingCommittee.members.keySet().parallelStream().forEach(m -> members.put(m, standingCommittee.members.get(m)));
        this.quorum = standingCommittee.getQuorum();
    }

    public Sem getResitLevel() {
        return resitLevel;
    }

    public int getApprovals() {
        return approvals.size();
    }

    public void approve(String approverName) {
        if (status == approved)
            throw new RuntimeException("This committee has already reached a quorum and is sealed. No more approvals are possible");

        Member member = members.get(approverName);

        if (member == null) {
            log.info("Attempt to approve by non-approver: {}", RED.color(approverName));
            throw new RuntimeException("'" + approverName + "' is not an approver for the '" + name + "' committee. Valid approvers are: " +
                    members.keySet().parallelStream().sorted().collect(joining(", ")));
        }

        approvals.add(new Approval(member));

        if (approvals.size() >= quorum)
            status = approved;

        log.info("env: {} approved by {}. Approvals: {}/{}. Status {}"
                , COMMITTEE.color(name)
                , MEMBER.color(approverName)
                , DATA2.color(getApprovals())
                , DATA.color(getQuorum())
                , STATUS.color(status.toString(), status == approved));
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        sitting("Sitting"), approved("Approved");

        private String status;

        Status(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }
}
