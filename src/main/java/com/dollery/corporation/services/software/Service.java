package com.dollery.corporation.services.software;

import java.util.HashMap;
import java.util.Map;

/**
 * A software service describes a service that will be fulfilled by one or more pods
 */
public class Service extends Software {
    private Map<String, Pod> pods = new HashMap<>();

    public Service(String name) {
        super(name);
    }

    public Service(String name, String tag) {
        super(name, tag);
    }

    public Service(String name, String tag, SemVer version) {
        super(name, tag, version);
    }

    @Override
    String getType() {
        return "Service";
    }
}
