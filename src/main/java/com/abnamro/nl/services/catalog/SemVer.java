package com.abnamro.nl.services.catalog;

public class SemVer {
    private Ver major = new Ver(Sem.major);
    private Ver minor = new Ver(Sem.minor);
    private Ver patch = new Ver(Sem.patch);

    public SemVer() {
    }

    private SemVer(Ver major, Ver minor, Ver patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public SemVer major() {
        return new SemVer(major.inc(), minor, patch);
    }

    public SemVer minor() {
        return new SemVer(major, minor.inc(), patch);
    }

    public SemVer patch() {
        return new SemVer(major, minor, patch.inc());
    }

    public int getMajor() {
        return major.value;
    }

    public int getMinor() {
        return minor.value;
    }

    public int getPatch() {
        return patch.value;
    }

}
