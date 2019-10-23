package com.dollery.corporation.services.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dollery.corporation.services.Colors.COMMITTEE;
import static com.dollery.corporation.services.Colors.DATA;
import static com.dollery.corporation.services.Colors.GREEN;
import static com.dollery.corporation.services.Colors.MEMBER;
import static com.dollery.corporation.services.Colors.STATUS;
import static com.dollery.corporation.services.catalog.Sem.patch;
import static com.dollery.corporation.services.catalog.StandingCommittee.Status.standing;
import static com.dollery.corporation.services.catalog.StandingCommittee.Status.unready;

/**
 * A committee is a group of at least 3 actors (people or machines) who get to decide if something is acceptable or not
 * each according to their own agenda. We have 2 uses for committees -- we call these standing and sitting. A standing
 * committee is the currently appointed members of that committee who can be called into action. A sitting committee is
 * a committee that is currently being asked for approvals. This separation occurs so that we can plan committee membership
 * without necessarily changing the people working on an in-flight approval process.
 * <p>
 */
public class StandingCommittee extends Committee {
    private static final Logger log = LoggerFactory.getLogger(StandingCommittee.class);
    protected Status status = unready;
    private Sem defaultResitEvery = patch;

    public StandingCommittee(String name) {
        super(name);
    }

    public StandingCommittee(String name, int quorum) {
        super(name);
        setQuorum(quorum);
    }

    public StandingCommittee setMinimumQuorum(int minimumQuorum) {
        if (minimumQuorum < 1) throw new RuntimeException("Everything needs at least one approver");
        if (minimumQuorum % 2 == 0) throw new RuntimeException("Odd numbers only for fairly obvious reasons");
        this.minimumQuorum = minimumQuorum;
        return this;
    }

    public StandingCommittee addMember(String memberName) {
        if (status != unready)
            throw new RuntimeException("You can only add members to committees before they sit");

        members.put(memberName, new Member(memberName));

        status = members.size() < quorum + 2 ? unready : standing;

        log.info("Committee: {}. New member: {}. Member count: {}. Quorum: {}. Status: {}",
                COMMITTEE.color(name),
                MEMBER.color(memberName),
                DATA.color(members.size()),
                GREEN.color(quorum),
                STATUS.color(status.toString()), status == unready);

        return this;
    }

    public SittingCommittee sit(Sem resitEvery) {
        return new SittingCommittee(this, resitEvery);
    }

    public SittingCommittee sit() {
        return sit(defaultResitEvery);
    }

    public Status getStatus() {
        return status;
    }

    public Sem getDefaultResitEvery() {
        return defaultResitEvery;
    }

    public void setDefaultResitEvery(Sem defaultResitEvery) {
        this.defaultResitEvery = defaultResitEvery;
    }

    public enum Status {
        unready("Unready"), standing("Standing");

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
