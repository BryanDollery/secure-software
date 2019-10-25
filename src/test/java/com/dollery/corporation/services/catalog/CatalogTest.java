package com.dollery.corporation.services.catalog;

import com.dollery.corporation.services.bus.EventBus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogTest {
    private EventBus bus = new EventBus();

    @Test
    void services() {
        Catalog c = new Catalog(bus);
        c.addService("a");
        c.addService("b");
        c.addRelationship("a", "b");
        String services = c.getServicesJson();
        assertEquals("{\"services\" : [\"a\",\"b\"]}", services);
    }

    @Test
    void relationship() {
        Catalog c = new Catalog(bus);
        c.addService("a");
        c.addService("b");
        c.addService("c");
        c.addService("d");
        c.addRelationship("a", "b");
        c.addRelationship("c", "d");
        c.addRelationship("a", "d");
        String relationships = c.getRelationshipsJson();
        assertEquals("[{\"consumer\":\"a\",\"provider\":\"b\"},{\"consumer\":\"a\",\"provider\":\"d\"},{\"consumer\":\"c\",\"provider\":\"d\"}]", relationships);
    }
}
