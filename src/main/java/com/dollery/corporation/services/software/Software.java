package com.dollery.corporation.services.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.dollery.corporation.services.Colors.DATA;
import static com.dollery.corporation.services.Colors.ID;
import static com.dollery.corporation.services.Colors.NAME;

/**
 * I'm taking the position that all software is a docker container. Then I can use the word 'binary' or image to describe the
 * artifact being deployed and not care if its really a binary or a script or something more exotic -- it's all just containers.
 */
public abstract class Software {
    private static final Logger log = LoggerFactory.getLogger(Software.class);
    private String id = UUID.randomUUID().toString();
    private String name;
    private String tag;
    private SemVer version;

    public Software(String name) {
        this(name, "latest");
    }

    public Software(String name, String tag) {
        this(name, tag, new SemVer());
    }

    public Software(String name, String tag, SemVer version) {
        this.name = name;
        this.tag = tag;
        this.version = version;
        log.info("New {}: {} {}", DATA.color(this.getType()), NAME.color(this.name), ID.color("(" + this.id + ")"));
    }

    abstract String getType();

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public SemVer getVersion() {
        return version;
    }
}
