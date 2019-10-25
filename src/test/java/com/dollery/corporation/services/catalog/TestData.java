package com.dollery.corporation.services.catalog;

import com.dollery.corporation.services.org.Enterprise;
import com.dollery.corporation.services.org.SoftwareOrganisation;

/**
 * Creates a vault, standing committees for arb and dev-team, with 3 and 1 quorums respectively, and
 */
public class TestData {

    public static final String DEV = "dev";
    public static final String ARB = "arb";
    public static final String DEV_TEAM = "dev-team";
    private Enterprise enterprise = new Enterprise("Acme Inc.");

    private final SoftwareOrganisation owner = enterprise.add(DEV_TEAM);

    public TestData() {
        StandingCommittee arb = owner.formCommittee(ARB);
        StandingCommittee devTeam = owner.formCommittee(DEV_TEAM, 1);

        // Add members to committees and reset quorum
        arb.addMember("Alice").addMember("Bob").addMember("Charlie").addMember("Dave").addMember("Enid");
        devTeam.addMember("Xavier").addMember("Yoris").addMember("Zebedee");

        // Add them as sitting approvers for the dev environment
        owner.sit("dev", ARB);
        owner.sit("dev", DEV_TEAM);

        // The dev environment now requires 3 arb approvals and 1 dev-team approval, at which point it will be marked as approved (and therefore can be deployed)
    }

    public void givenAnApprovedRelationship() {
        owner.getEnv("dev").addService("a").addService("b").addRelationship("a", "b");
        owner.getEnv("dev").approve(ARB, "Alice").approve(ARB, "Bob").approve(ARB, "Dave");
        owner.getEnv("dev").approve(DEV_TEAM, "Zebedee");
    }

    public ControlledEnvironment env() {
        return owner.getEnv("dev");
    }
}
