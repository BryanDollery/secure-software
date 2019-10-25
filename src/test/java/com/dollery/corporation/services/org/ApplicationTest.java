package com.dollery.corporation.services.org;

import com.dollery.corporation.services.catalog.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class ApplicationTest {

    @Test
    void escalateApprovals() {

    }

    @Test
    void willBlockIfUnapproved() {
        // given
        Application app = new Application("wibble");

        TestData devEnv = new TestData();
        TestData testEnv = new TestData();
        TestData prodEnv = new TestData();

        app.addEnvironmentToPhase(Application.Lifecycle.Phase.dev, devEnv.env());
        app.addEnvironmentToPhase(Application.Lifecycle.Phase.test, testEnv.env());
        app.addEnvironmentToPhase(Application.Lifecycle.Phase.prod, prodEnv.env());

        Assertions.assertEquals(Application.Lifecycle.Phase.dev, app.getPhase());
        Assertions.assertEquals(Application.Lifecycle.State.pre, app.getState());

        devEnv.givenAnApprovedRelationship();

        // when
        app.advance();

        // then
        Assertions.assertEquals(Application.Lifecycle.Phase.dev, app.getPhase());
        Assertions.assertEquals(Application.Lifecycle.State.ready, app.getState());

        // when
        app.advance();

        // then
        Assertions.assertEquals(Application.Lifecycle.Phase.dev, app.getPhase());
        Assertions.assertEquals(Application.Lifecycle.State.post, app.getState());

        // when
        app.advance();

        // then
        Assertions.assertEquals(Application.Lifecycle.Phase.dev, app.getPhase());
        Assertions.assertEquals(Application.Lifecycle.State.end, app.getState());

        // when
        app.advance();

        // then
        Assertions.assertEquals(Application.Lifecycle.Phase.test, app.getPhase());
        Assertions.assertEquals(Application.Lifecycle.State.pre, app.getState());

        // when
        try {
            app.advance();
            fail();
        } catch (RuntimeException e) {
            // pass
        }
    }
}