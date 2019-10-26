package com.dollery.corporation.services.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * I'm taking the position that all software is a docker container. Then I can use the word 'binary' or image to describe the
 * artifact being deployed and not care if its really a binary or a script or something more exotic -- it's all just containers.
 */
public class Image extends Software {
    private static final Logger log = LoggerFactory.getLogger(Image.class);

    public Image(String name) {
        super(name);
    }

    public Image(String name, String tag) {
        super(name, tag);
    }

    public Image(String name, String tag, SemVer version) {
        super(name, tag, version);
    }

    @Override
    String getType() {
        return "image";
    }
}
