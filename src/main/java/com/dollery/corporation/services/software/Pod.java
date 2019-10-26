package com.dollery.corporation.services.software;

import java.util.HashMap;
import java.util.Map;

/**
 * A pod is a collection of containers
 */
public class Pod extends Software {
    private Map<String, Container> containers = new HashMap<>();
    private String source;

    public Pod(String name, String source) {
        super(name);
        this.source = source;
    }

    public Pod(String name, String tag, String source) {
        super(name, tag);
        this.source = source;
    }

    public Pod(String name, String tag, SemVer version, String source) {
        super(name, tag, version);
        this.source = source;
    }

    @Override
    String getType() {
        return "pod";
    }
}
