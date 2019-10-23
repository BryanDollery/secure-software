package com.dollery.corporation.services.org;

import com.dollery.corporation.services.catalog.ControlledEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A "software organisation" is a bounded context (ddd) that contains three software contexts:
 * <ol> <li>Development</li> <li>Test</li> <li>Production</li> </ol>
 * It both publishes and consumes every API it knows about.
 * <p>
 * In the real world, this might be a single business, if all the systems were tightly integrated
 * But, because this is a PerOrg we can model all sorts of interesting structures including those that currently exist
 * and those that don't.
 * <p>
 * e.g. Environmental Abstraction
 * A corporation has a single production context -- this is the aggregate of all systems currently performing
 * duties that are considered necessary for the running of the business and its interactions with its clients. In a bank,
 * this might be all the customer facing products across all divisions of the bank and all classes of product deployed
 * to many different production environments, along with all internal production systems that ultimately give rise to the
 * <p>
 * A corporation has a single test context. This is almost identical to production, except that its used for quality
 * control purposes. When sufficient quality is achieved, this context is promoted to the production context.
 * <p>
 * A corporation has a single development context. This is nothing like production. It is the aggregate of every bit of
 * software and every environment that isn't being used for production or testing purposes.
 * <p>
 * To implement this model, I have created this class, Software Organisation. It holds a single ControlledEnvironment
 * called "context". I have created three children of this org, dev, test, and prod, each of which is the name of a
 * sub-organisation.
 */
public class SoftwareOrganisation extends PerOrg {
    private static final Logger log = LoggerFactory.getLogger(SoftwareOrganisation.class);
    private ControlledEnvironment context;

    public SoftwareOrganisation(String name) {
        super(name);

        this.add("dev");
        this.add("test");
        this.add("prod");
    }

    @Override
    public PerOrg add(String name) {
        add(new SoftwareOrganisation(name));
        return this;
    }
}
