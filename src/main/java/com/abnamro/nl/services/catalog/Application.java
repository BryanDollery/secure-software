package com.abnamro.nl.services.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.abnamro.nl.services.catalog.Application.Lifecycle.Phase.dev;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.State.pre;
import static com.abnamro.nl.services.catalog.Application.Lifecycle.State.ready;

/**
 * An application is a collection of related services and environments tied up with an application lifecycle
 */
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private String id;
    private String name;
    private Map<Lifecycle.Phase, ControlledEnvironment> envs = new HashMap<>();
    private Lifecycle lifecycle;

    public Application(String name, Lifecycle.Phase phase, Lifecycle.State state) {
        id = UUID.randomUUID().toString();
        this.name = name;
        lifecycle = new Lifecycle(phase, state);
    }

    public Application(String name) {
        this(name, dev, pre);
    }

    public void addEnvironmentToPhase(Lifecycle.Phase phase, ControlledEnvironment env) {
        envs.put(phase, env);
    }

    public void advance() {
        ControlledEnvironment env = envs.get(lifecycle.getPhase());

        if (lifecycle.getState().next() == ready && !env.isApproved())
            throw new RuntimeException("Unapproved advancement to ready for environment " + env.getName());

        try {
            lifecycle.nextState();
        } catch (RuntimeException e) {
            lifecycle.nextPhase();
        }
    }

    public Lifecycle.Phase getPhase() {
        return lifecycle.getPhase();
    }

    public Lifecycle.State getState() {
        return lifecycle.getState();
    }

    static class Lifecycle {

        private Phase phase = dev;
        private State state = pre;

        Lifecycle() {
            this(dev, pre);
        }

        Lifecycle(final Phase phase, final State state) {
            this.phase = phase;
            this.state = state;
        }

        public Phase getPhase() {
            return phase;
        }

        public State getState() {
            return state;
        }

        private void nextState() {
            State next = state.next();

            if (next == pre) throw new RuntimeException("end");

            state = next;
        }

        private void nextPhase() {
            Phase next = phase.next();

            if (next == dev) throw new RuntimeException("decom");

            phase = next;
            state = pre;
        }

        public enum Phase {
            // Could map to dev, test, prod in the old world
            dev, test, prod, decom;

            private static Phase[] vals = values();

            public Phase next() {
                return vals[(this.ordinal() + 1) % vals.length];
            }
        }

        /**
         * The current state of an application. An application can be in three states:
         * starting, running, and finished -- these map to pre, *, and post states in
         * this abstraction.
         */
        public enum State {
            pre, ready, post, end;

            private static State[] vals = values();

            public State next() {
                return vals[(this.ordinal() + 1) % vals.length];
            }
        }

    }
}