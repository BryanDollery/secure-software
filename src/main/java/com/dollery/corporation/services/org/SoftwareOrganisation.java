package com.dollery.corporation.services.org;

import com.dollery.corporation.services.Colors;
import com.dollery.corporation.services.bus.EventBus;
import com.dollery.corporation.services.catalog.ControlledEnvironment;
import com.dollery.corporation.services.catalog.SittingCommittee;
import com.dollery.corporation.services.catalog.StandingCommittee;
import com.dollery.corporation.services.catalog.StandingCommittees;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A "software organisation" is a bounded context (ddd) that contains a dev, test, and prod environment. Remember that
 * these environments are not necessarily physical -- they are actually just collections of approvals for immutable
 * groups of related services to provide services to the organisation. If we build an environment and it works first time
 * we could get permission to route prod traffic through it, in an emergency (this is really only true on software-
 * defined networks).
 */
public class SoftwareOrganisation extends PerOrg<SoftwareOrganisation> {
    private static final Logger log = LoggerFactory.getLogger(SoftwareOrganisation.class);

    private StandingCommittees standingCommittees = new StandingCommittees();
    private Map<String, ControlledEnvironment> envs = new HashMap<>();
    private EventBus bus;

    public SoftwareOrganisation(String name, EventBus bus) {
        super(name);

        this.bus = bus;

        envs.put("dev", new ControlledEnvironment("dev", this));
        envs.put("test", new ControlledEnvironment("test", this));
        envs.put("prod", new ControlledEnvironment("prod", this));

        log.info("New software org: {}", Colors.BLUE.color(name));
    }

    @Override
    public SoftwareOrganisation add(String name) {
        SoftwareOrganisation child = new SoftwareOrganisation(name, bus);
        add(child);
        return child;
    }

    public StandingCommittees getStandingCommittees() {
        return standingCommittees;
    }

    public StandingCommittee getStandingCommittee(String name) {
        return standingCommittees.get(name);
    }

    public SittingCommittee sit(String name) {
        return standingCommittees.get(name).sit();
    }

    public EventBus getBus() {
        return bus;
    }

    public StandingCommittee formCommittee(String name) {
        StandingCommittee committee = new StandingCommittee(name);
        standingCommittees.add(committee);
        return committee;
    }

    public StandingCommittee formCommittee(String name, int quorum) {
        StandingCommittee committee = new StandingCommittee(name, quorum);
        standingCommittees.add(committee);
        return committee;
    }

    public ControlledEnvironment getEnv(String name) {
        return envs.get(name);
    }

    public void sit(String env, String committee) {
        envs.get(env).sit(committee);
    }
}
