package com.dollery.corporation.services.catalog;

import com.dollery.corporation.services.org.SoftwareOrganisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dollery.corporation.services.Colors.RED;
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

    private String id = randomUUID().toString();
    private String name;
    private String tag;
    private Map<String, SittingCommittee> sittingCommittees = new HashMap<>();
    private Map<String, Service> services = new HashMap<>();
    private Map<String, List<Relationship>> relationships = new HashMap<>();
    private SemVer version;
    private SoftwareOrganisation owner;

    /**
     * Automatically applies the tag 'latest' to this env
     *
     * @param name
     * @param owner
     */
    public ControlledEnvironment(String name, SoftwareOrganisation owner) {
        this(name, "latest", owner);
    }

    /**
     * Automatically applies a new version number of 0.0.0
     *
     * @param name
     * @param tag
     * @param owner
     */
    public ControlledEnvironment(String name, String tag, SoftwareOrganisation owner) {
        this(name, tag, owner, new SemVer());
    }

    /**
     * Only the id is automatically generated at this level
     *
     * @param name
     * @param tag
     * @param owner
     * @param version
     */
    public ControlledEnvironment(String name, String tag, SoftwareOrganisation owner, SemVer version) {
        this.name = name;
        this.tag = tag;
        this.owner = owner;
        this.version = version;
    }

    public Committee sit(String name) {
        SittingCommittee sittingCommittee = owner.sit(name);
        sittingCommittees.put(name, sittingCommittee);
        return sittingCommittee;
    }

    public String getName() {
        return name + "/" + tag;
    }

    public String getSimpleName() {
        return name;
    }

    public int getQuorum(String committeeName) {
        return sittingCommittees.get(committeeName).getQuorum();
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
        SittingCommittee committee = sittingCommittees.get(committeeName);

        if (committee == null) {
            log.debug("{} is not a committee associated with this environment", RED.color(committeeName));
            throw new RuntimeException(committeeName + " is not a committee associated with this environment");
        }

        return committee;
    }

    public int getApprovals(String name) {
        return findCommittee(name).getApprovals();
    }

    public boolean isApproved(String name) {
        return findCommittee(name).getStatus() == SittingCommittee.Status.approved;
    }

    public boolean isApproved() {
        return !sittingCommittees.values().parallelStream().anyMatch(c -> c.getStatus() != SittingCommittee.Status.approved);
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
        return new ControlledEnvironment(name, tag, owner, version.patch());
    }

    public ControlledEnvironment minor() {
        return new ControlledEnvironment(name, tag, owner, version.minor());
    }

    public ControlledEnvironment major() {
        return new ControlledEnvironment(name, tag, owner, version.major());
    }

}
