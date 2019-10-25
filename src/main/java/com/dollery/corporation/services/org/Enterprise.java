package com.dollery.corporation.services.org;

import com.dollery.corporation.services.bus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enterprise extends SoftwareOrganisation {
    private static final Logger log = LoggerFactory.getLogger(Enterprise.class);

    public Enterprise(String name) {
        super(name, new EventBus());
    }
}
