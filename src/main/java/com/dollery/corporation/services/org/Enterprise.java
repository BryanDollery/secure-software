package com.dollery.corporation.services.org;

import com.dollery.corporation.services.Output;
import com.dollery.corporation.services.bus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.dollery.corporation.services.Colors.DATA;
import static com.dollery.corporation.services.Colors.KEY;

public class Enterprise extends SoftwareOrganisation {
    private static final Logger log = LoggerFactory.getLogger(Enterprise.class);

    /**
     * The enterprise is the organisation element that is at the top of the hierarchy. This is where the enterprise
     * is named and where the event bus is created. More formally, an enterprise is the collection of PerOrgs
     * that share an event-bus.
     *
     * @param name
     */
    public Enterprise(String name) {
        super(name, new EventBus());
    }

    public void dump() {
        Set<SoftwareOrganisation> kids = this.get();

        for (SoftwareOrganisation kid : kids)
            System.out.println(Output.colorizeJson(kid.toString(), KEY, DATA));
    }
}
