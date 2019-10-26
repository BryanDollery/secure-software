package com.dollery.corporation.services.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Container extends Software {
    private static final Logger log = LoggerFactory.getLogger(Container.class);

    public Container(String name) {
        super(name);
    }

    public Container(String name, String tag) {
        super(name, tag);
    }

    public Container(String name, String tag, SemVer version) {
        super(name, tag, version);
    }

    @Override
    String getType() {
        return "container";
    }
}
