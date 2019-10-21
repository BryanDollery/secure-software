package com.abnamro.nl.services.catalog;

import com.abnamro.nl.services.Colors;
import com.abnamro.nl.services.bus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abnamro.nl.services.Colors.RESET;
import static com.abnamro.nl.services.catalog.SittingCommittee.Status.approved;
import static java.util.UUID.randomUUID;

/**
 * A controlled environment may represent one of two things, depending on the context in which it runs. In a classic
 * context this will represent a real environment, "dev", "test", or "prod". In more modern architectures, we will have
 * a single deployment tested, and if it passes we will route production traffic to it, so it will pass through phases of
 * use -- test, then production. In the latter case, a controlled environment represents a phase in the lifecycle if a single
 * real deployment.
 * <p>
 * From the perspective of the controlled environment however, both use-cases look the same. The 'phase' terminology from
 * the more modern use-case will work (at a stretch) for the classic one too, so if we stick with that this calss doesn't
 * need to know which paradigm its being used for -- physical environments, or phases of a single one.
 */
@SuppressWarnings("WeakerAccess")
public class ControlledEnvironment {
    private static final Logger log = LoggerFactory.getLogger(ControlledEnvironment.class);

    private String id;
    private EventBus bus;
    private String name;
    private String tag;
    private StandingCommittees standingCommittees;
    private Map<String, SittingCommittee> committees = new HashMap<>();
    private Map<String, Service> services = new HashMap<>();
    private Map<String, List<Relationship>> relationships = new HashMap<>();
    private SemVer version;

    public ControlledEnvironment(EventBus bus, String name, String tag, StandingCommittees standingCommittees) {
        this(bus, name, tag, standingCommittees, new SemVer());
    }

    private ControlledEnvironment(EventBus bus, String name, String tag, StandingCommittees standingCommittees, SemVer version) {
        id = randomUUID().toString();

        this.bus = bus;
        this.name = name;
        this.tag = tag;
        this.standingCommittees = standingCommittees;
        this.version = version;
    }

    public Committee addCommittee(String name) {
        SittingCommittee committee = standingCommittees.get(name).sit();
        committees.put(name, committee);
        return committee;
    }

    public String getName() {
        return name + "/" + tag;
    }

    public String getSimpleName() {
        return name;
    }

    public int getQuorum(String committeeName) {
        return committees.get(committeeName).getQuorum();
    }

    public ControlledEnvironment addService(String name) {
        services.put(name, new Service(name));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ControlledEnvironment addRelationship(String client, String server) {
        if (!relationships.containsKey(client))
            relationships.put(client, new ArrayList<>());

        relationships.get(client).add(new Relationship(services.get(client), services.get(server)));

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ControlledEnvironment approve(String committeeName, String approverName) {
        findCommittee(committeeName).approve(approverName);
        return this;
    }

    private SittingCommittee findCommittee(String committeeName) {
        SittingCommittee committee = committees.get(committeeName);

        if (committee == null) {
            log.debug(Colors.RED + "{}" + RESET + " is not a committee associated with this environment", committeeName);
            throw new RuntimeException(committeeName + " is not a committee associated with this environment");
        }

        return committee;
    }

    public int getApprovals(String name) {
        return findCommittee(name).getApprovals();
    }

    public boolean isApproved(String name) {
        return findCommittee(name).getStatus() == approved;
    }

    public boolean isApproved() {
        return !committees.values().parallelStream().anyMatch(c -> c.getStatus() != approved);
    }

    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public SemVer getVersion() {
        return version;
    }

    public ControlledEnvironment patch() {
        return new ControlledEnvironment(bus, name, tag, standingCommittees, version.patch());
    }

    public ControlledEnvironment minor() {
        return new ControlledEnvironment(bus, name, tag, standingCommittees, version.minor());
    }

    public ControlledEnvironment major() {
        return new ControlledEnvironment(bus, name, tag, standingCommittees, version.major());
    }
}
