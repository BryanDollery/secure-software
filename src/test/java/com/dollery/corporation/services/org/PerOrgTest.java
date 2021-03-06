package com.dollery.corporation.services.org;

import org.junit.jupiter.api.Test;

import static com.dollery.corporation.services.Output.shadeSymbols;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PerOrgTest {
    @Test
    void thing() {
        TestOrg aab = new TestOrg("Acme Agricultural Bank");
        aab.add("a").add("b").add("c");
        PerOrg b = aab.get("b");
        b.add("ba").add("bb").add("bc");
        PerOrg ba = b.get("ba");
        ba.add("baa").add("bab").add("bac");
        System.out.println(shadeSymbols(aab.toString()));
        PerOrg aaa = ba.get("bab");
        assertNotNull(aaa);
    }

    private static class TestOrg extends PerOrg<TestOrg> {
        TestOrg(String name) {
            super(name);
        }

        @Override
        public TestOrg add(String name) {
            super.add(new TestOrg(name));
            return this;
        }
    }
}
