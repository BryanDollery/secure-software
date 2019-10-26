package com.dollery.corporation.services.org;

import com.dollery.corporation.services.Output;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.dollery.corporation.services.Colors.DATA;
import static com.dollery.corporation.services.Colors.KEY;

class EnterpriseTest {
    @Test
    void bank() {

        // Let's model a pure cloud bank, with 4 divisions, a single production environment, and a pull process
        // No physical facilities or presence at all


        Enterprise acme = new Enterprise("Acme");

        SoftwareOrganisation bank = acme.add("bank"); // product
        SoftwareOrganisation retail = bank.add("retail");
        SoftwareOrganisation commercial = bank.add("commercial");
        SoftwareOrganisation personal = bank.add("personal");
        SoftwareOrganisation investment = bank.add("investment");

        SoftwareOrganisation delivery = acme.add("delivery");

        SoftwareOrganisation hr = delivery.add("human-resources");
        SoftwareOrganisation am = delivery.add("asset-management");
        SoftwareOrganisation legal = delivery.add("legal");
        SoftwareOrganisation risk = delivery.add("risk");
        SoftwareOrganisation it = delivery.add("IT");

        SoftwareOrganisation ciso = it.add("CISO");
        SoftwareOrganisation ea = it.add("EA");

        SoftwareOrganisation modelling = ea.add("modelling");
        SoftwareOrganisation currentState = ea.add("current-state");
        SoftwareOrganisation futureState = ea.add("future-state");
        SoftwareOrganisation transitionState = ea.add("transition-state");
        SoftwareOrganisation strategy = futureState.add("srategy"); // direction
        SoftwareOrganisation partnerships = strategy.add("partnerships"); // active
        SoftwareOrganisation planning = ea.add("planning");
        SoftwareOrganisation infrastructure = it.add("infrastructure"); // physical
        SoftwareOrganisation platform = it.add("platform");
        SoftwareOrganisation services = it.add("services");
        SoftwareOrganisation integration = it.add("integration"); // integration integrates software into the bank
        SoftwareOrganisation development = it.add("development"); // development creates new software

        SoftwareOrganisation comms = services.add("communications");
        SoftwareOrganisation video = comms.add("video");
        SoftwareOrganisation chat = comms.add("chat");
        SoftwareOrganisation email = comms.add("email");
        SoftwareOrganisation wiki = comms.add("wiki");
        SoftwareOrganisation tasks = comms.add("tasks");

        acme.dump();

    }

    @Test
    void keep() {

        // Let's model a bank, with


        Enterprise acme = new Enterprise("Acme");

        SoftwareOrganisation bank = acme.add("bank"); // product
        SoftwareOrganisation retail = bank.add("retail");
        SoftwareOrganisation commercial = bank.add("commercial");
        SoftwareOrganisation personal = bank.add("personal");
        SoftwareOrganisation investment = bank.add("investment");

        SoftwareOrganisation delivery = acme.add("delivery");
        SoftwareOrganisation it = delivery.add("IT");
        SoftwareOrganisation ciso = it.add("CISO");
        SoftwareOrganisation ea = it.add("EA");
        SoftwareOrganisation modelling = ea.add("modelling");
        SoftwareOrganisation currentState = ea.add("current-state");
        SoftwareOrganisation futureState = ea.add("future-state");
        SoftwareOrganisation transitionState = ea.add("transition-state");
        SoftwareOrganisation strategy = futureState.add("srategy"); // direction
        SoftwareOrganisation partnerships = strategy.add("partnerships"); // active
        SoftwareOrganisation planning = ea.add("planning");
        SoftwareOrganisation infrastructure = it.add("infrastructure"); // physical
        SoftwareOrganisation platform = it.add("platform");
        SoftwareOrganisation services = it.add("services");
        SoftwareOrganisation integration = it.add("integration"); // integration integrates software into the bank
        SoftwareOrganisation development = it.add("development"); // development creates new software
        SoftwareOrganisation hr = delivery.add("human-resources");
        SoftwareOrganisation am = delivery.add("asset-management");
        SoftwareOrganisation fm = am.add("facilities-management");


        Set<SoftwareOrganisation> kids = acme.get();

        for (SoftwareOrganisation kid : kids) {
            System.out.println(Output.colorizeJson(kid.toString(), KEY, DATA));
        }


    }
}