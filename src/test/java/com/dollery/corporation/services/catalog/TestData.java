package com.dollery.corporation.services.catalog;

import com.dollery.corporation.services.bus.EventBus;

/**
 * Creates a vault, standing committees for arb and dev-team, with 3 and 1 quorums respectively, and
 */
public class TestData {

    public static final String DEV = "dev";
    public static final String ARB = "arb";
    public static final String DEV_TEAM = "dev-team";
    public final StandingCommittee arb = new StandingCommittee(ARB);
    public final StandingCommittee devTeam = new StandingCommittee(DEV_TEAM, 1);
    public final EventBus bus = new EventBus();
    public final StandingCommittees standingCommittees = new StandingCommittees(bus);
    public final ControlledEnvironment env = new ControlledEnvironment(bus, DEV, "latest", standingCommittees);

    public TestData() {
        // Add members to committees and reset quorum
        arb.addMember("Alice").addMember("Bob").addMember("Charlie").addMember("Dave").addMember("Enid");
        devTeam.addMember("Xavier").addMember("Yoris").addMember("Zebedee");

        // Committees are standing
        standingCommittees.add(arb);
        standingCommittees.add(devTeam);

        // Add them as sitting approvers for the environment
        env.addCommittee(ARB);
        env.addCommittee(DEV_TEAM);

        // The dev environment now requires 3 arb approvals and 1 dev-team approval, at which point it will be marked as approved (and therefore can be deployed)
    }

    public void givenAnApprovedRelationship() {
        this.env.addService("a").addService("b").addRelationship("a", "b");
        this.env.approve(ARB, "Alice").approve(ARB, "Bob").approve(ARB, "Dave");
        this.env.approve(DEV_TEAM, "Zebedee");
    }
}