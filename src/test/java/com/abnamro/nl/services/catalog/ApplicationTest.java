package com.abnamro.nl.services.catalog;

import org.junit.jupiter.api.Test;

import static com.abnamro.nl.services.catalog.Application.Lifecycle.Phase.dev;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.Phase.prod;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.Phase.test;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.State.end;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.State.post;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.State.pre;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.State.ready;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ApplicationTest {

    @Test
    void willBlockIfUnapproved() {
        // given
        Application app = new Application("wibble");

        TestData devEnv = new TestData();
        TestData testEnv = new TestData();
        TestData prodEnv = new TestData();

        app.addEnvironmentToPhase(dev, devEnv.env);
        app.addEnvironmentToPhase(test, testEnv.env);
        app.addEnvironmentToPhase(prod, prodEnv.env);

        assertEquals(dev, app.getPhase());
        assertEquals(pre, app.getState());

        devEnv.givenAnApprovedRelationship();

        // when
        app.advance();

        // then
        assertEquals(dev, app.getPhase());
        assertEquals(ready, app.getState());

        // when
        app.advance();

        // then
        assertEquals(dev, app.getPhase());
        assertEquals(post, app.getState());

        // when
        app.advance();

        // then
        assertEquals(dev, app.getPhase());
        assertEquals(end, app.getState());

        // when
        app.advance();

        // then
        assertEquals(test, app.getPhase());
        assertEquals(pre, app.getState());

        // when
        try {
            app.advance();
            fail();
        } catch (RuntimeException e) {
            // pass
        }
    }
}