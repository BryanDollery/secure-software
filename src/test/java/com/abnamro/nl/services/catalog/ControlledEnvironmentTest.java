package com.abnamro.nl.services.catalog;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.abnamro.nl.services.catalog.TestData.ARB;
import static com.abnamro.nl.services.catalog.TestData.DEV_TEAM;
import static org.junit.jupiter.api.Assertions.*;

class ControlledEnvironmentTest {

    @Test
    void committeesHaveQuorums() {
        TestData data = new TestData();

        assertEquals(3, data.env.getQuorum(ARB));
        assertEquals(1, data.env.getQuorum(DEV_TEAM));
    }

    // This test proves that the 'givenApprovedRelationships" method in the TestData works. It's basically the same code
    // but broken down into stages so that assertions can be made about the state of the model at each step
    @Test
    void simpleApprovalsWork() {
        TestData data = new TestData();

        data.env.addService("a").addService("b").addRelationship("a", "b");
        assertFalse(data.env.isApproved(), "There should have been no approvals yet");

        // arb
        data.env.approve(ARB, "Alice").approve(ARB, "Bob").approve(ARB, "Dave");
        assertEquals(3, data.env.getApprovals(ARB), "arb should have had 3 approvals now");
        assertTrue(data.env.isApproved(ARB));
        assertFalse(data.env.isApproved(), "We have ARB approvals, but the dev-team are now the blocker");

        // dev-team
        data.env.approve(DEV_TEAM, "Zebedee");
        assertTrue(data.env.isApproved(DEV_TEAM));
        assertTrue(data.env.isApproved());
    }

    @Test
    void devTestProdAreLifecycleStepsInAnEnvironment() {
        // deploy simply pushes software to an environment; release is what makes it live

        TestData data = new TestData();
    }

    private void keep() {
        class Details {
            private String committee;
            private int quorum;
            private int members;
            private int approvals;

            public Details(String committee, int quorum, int members, int approvals) {
                this.setCommittee(committee);
                this.setQuorum(quorum);
                this.setMembers(members);
                this.setApprovals(approvals);
            }

            public String getCommittee() {
                return committee;
            }

            public void setCommittee(String committee) {
                this.committee = committee;
            }

            public int getQuorum() {
                return quorum;
            }

            public void setQuorum(int quorum) {
                this.quorum = quorum;
            }

            public int getMembers() {
                return members;
            }

            public void setMembers(int members) {
                this.members = members;
            }

            public int getApprovals() {
                return approvals;
            }

            public void setApprovals(int approvals) {
                this.approvals = approvals;
            }
        }

        class Approvals {
            private Set<Object> approvals = new HashSet<>();

            public Set<Object> getApprovals() {
                return approvals;
            }

            public void setApprovals(Set<Object> approvals) {
                this.approvals = approvals;
            }
        }

        Details d = new Details("arb", 3, 5, 3);
        Details e = new Details("dev-team", 1, 3, 1);
        Approvals a = new Approvals();
        a.getApprovals().add(d);
        a.getApprovals().add(e);

    }
}
