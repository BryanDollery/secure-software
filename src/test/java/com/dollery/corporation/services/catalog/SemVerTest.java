package com.dollery.corporation.services.catalog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemVerTest {
    @Test
    void thing() {
        SemVer a = new SemVer();
        ControlledEnvironment env = new ControlledEnvironment(null, null, null, null);
        assertEquals(0, env.getVersion().getMajor());
        assertEquals(0, env.getVersion().getMinor());
        assertEquals(0, env.getVersion().getPatch());
        env = env.major();
        assertEquals(1, env.getVersion().getMajor());
        assertEquals(0, env.getVersion().getMinor());
        assertEquals(0, env.getVersion().getPatch());
        env = env.minor();
        assertEquals(1, env.getVersion().getMajor());
        assertEquals(1, env.getVersion().getMinor());
        assertEquals(0, env.getVersion().getPatch());
        env = env.patch();
        assertEquals(1, env.getVersion().getMajor());
        assertEquals(1, env.getVersion().getMinor());
        assertEquals(1, env.getVersion().getPatch());
        env = env.minor();
        assertEquals(1, env.getVersion().getMajor());
        assertEquals(2, env.getVersion().getMinor());
        assertEquals(1, env.getVersion().getPatch());
    }
}