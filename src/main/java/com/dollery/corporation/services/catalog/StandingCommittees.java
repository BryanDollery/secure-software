package com.dollery.corporation.services.catalog;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of committees that are ready to stand. These committees are cloned into an controlled environment meaning
 * that we can alter the makeup of a committee without removing an individual's responsibility to an in flight approval
 * process. We can change the in-flight committee's too, but the important thing is that a change there is independent
 * from a change here.
 */
public class StandingCommittees {
    private final Map<String, StandingCommittee> committees = new HashMap<>();

    public StandingCommittees add(StandingCommittee committee) {
        committees.put(committee.getName(), committee);
        return this;
    }

    public StandingCommittee get(String name) {
        return committees.get(name);
    }

    public SittingCommittee stand(String name) {
        StandingCommittee committee = committees.get(name);
        return committee.sit();
    }
}
